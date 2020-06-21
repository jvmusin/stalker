package musin.socialstalker.updater;

import lombok.RequiredArgsConstructor;
import musin.socialstalker.config.NetworkProperties;
import musin.socialstalker.db.IdFactory;
import musin.socialstalker.db.model.Monitoring;
import musin.socialstalker.db.model.Stalker;
import musin.socialstalker.db.repository.MonitoringRepository;
import musin.socialstalker.notifier.NotifiableUpdate;
import musin.socialstalker.relation.Update;
import musin.socialstalker.relation.UpdateFactory;
import musin.socialstalker.relation.User;
import musin.socialstalker.relation.list.RelationList;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SeekerServiceBase<
    ID,
    TUser extends User<ID>,
    TRelationType,
    TUpdate extends Update<TUser, TRelationType>,
    TRelationList extends RelationList<TUser, TRelationType>,
    TNotifiableUpdate extends NotifiableUpdate<TUser, TRelationType>>
    implements SeekerService<ID> {

  private final Stalker stalker;
  private final MonitoringRepository monitoringRepository;
  private final NetworkProperties properties;
  private final IdFactory<ID> idFactory;
  private final RelationListPuller<ID, TRelationList> relationListPuller;
  private final UpdateService<ID, TUpdate, TRelationList, TNotifiableUpdate> updateService;
  private final UpdateFactory<TUser, TRelationType, TUpdate> updateFactory;

  @Override
  @Transactional
  public List<ID> findAllTargets() {
    return monitoringRepository.findAllByStalkerAndNetwork(stalker, properties.getNetwork()).stream()
        .map(seeker -> idFactory.parse(seeker.getTarget()))
        .collect(toList());
  }

  @Override
  @Transactional
  public void addTarget(ID userId) {
    TRelationList list = relationListPuller.pull(userId).join();
    updateService.saveAll(list.asUpdates(updateFactory).collect(toList()), userId);
    monitoringRepository.save(new Monitoring(null, stalker, properties.getNetwork(), userId.toString()));
  }

  @Override
  @Transactional
  public void deleteTarget(ID userId) {
    updateService.removeAllByTarget(userId);
    monitoringRepository.deleteByStalkerAndTarget(stalker, userId.toString());
  }
}
