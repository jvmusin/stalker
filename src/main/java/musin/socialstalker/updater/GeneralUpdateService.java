package musin.socialstalker.updater;

import musin.socialstalker.db.model.Stalker;
import musin.socialstalker.notifier.NotifiableUpdate;
import musin.socialstalker.relation.Update;
import musin.socialstalker.relation.list.RelationList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GeneralUpdateService<ID, TRelationType> {

  List<NotifiableUpdate<TRelationType>> saveAll(Stalker stalker, List<Update<TRelationType>> updates, ID target);

  void removeAllByTarget(Stalker stalker, ID target);

  CompletableFuture<RelationList<TRelationType>> buildList(Stalker stalker, ID target);
}
