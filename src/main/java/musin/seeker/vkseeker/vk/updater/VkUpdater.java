package musin.seeker.vkseeker.vk.updater;

import lombok.RequiredArgsConstructor;
import musin.seeker.vkseeker.db.RelationChangeService;
import musin.seeker.vkseeker.db.SeekerService;
import musin.seeker.vkseeker.db.model.RelationChange;
import musin.seeker.vkseeker.notifier.UpdateNotifier;
import musin.seeker.vkseeker.vk.api.VkApi;
import musin.seeker.vkseeker.vk.relation.VkRelation;
import musin.seeker.vkseeker.vk.relation.VkRelationList;
import musin.seeker.vkseeker.vk.relation.VkRelationUpdate;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class VkUpdater implements Runnable {

  private final SeekerService seekerService;
  private final RelationChangeService relationChangeService;
  private final VkApi vkApi;
  private final List<UpdateNotifier<VkUpdate>> notifiers;
  private final TaskExecutor taskExecutor;
  private final VkUpdateFactory vkUpdateFactory;

  private static List<RelationChange> toDbChanges(int owner, Stream<VkRelationUpdate> changes) {
    return changes.map(update -> update.toDb(owner)).collect(toList());
  }

  @Override
  public void run() {
    seekerService.findAll().forEach(s -> taskExecutor.execute(() -> run(s.getOwner())));
  }

  private void run(int owner) {
    CompletableFuture<VkRelationList> was = relationChangeService.findAllByOwner(owner)
        .thenApply(changes -> changes.stream().map(VkRelation::fromDb))
        .thenApply(VkRelationList::new);

    CompletableFuture<VkRelationList> now = vkApi.loadRelationsAsync(owner);

    was.thenCombine(now, VkRelationList::updates)
        .thenApply(updates -> relationChangeService.saveAll(toDbChanges(owner, updates)))
        .thenApply(this::toVkUpdates)
        .thenAccept(differences -> notifiers.forEach(notifier -> notifier.notify(differences)));
  }

  private List<VkUpdate> toVkUpdates(List<RelationChange> changes) {
    return changes.stream()
        .map(vkUpdateFactory::createUpdate)
        .collect(toList());
  }
}