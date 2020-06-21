package musin.seeker.updater;

import musin.seeker.notifier.NotifiableUpdate;
import musin.seeker.relation.Update;
import musin.seeker.relation.list.RelationList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UpdateService<
    ID,
    TUpdate extends Update<?, ?>,
    TRelationList extends RelationList<?, ?>,
    TNotifiableUpdate extends NotifiableUpdate<?, ?>> {

  List<TNotifiableUpdate> saveAll(List<? extends TUpdate> updates, ID target);

  void removeAllByTarget(ID target);

  CompletableFuture<TRelationList> buildList(ID target);
}
