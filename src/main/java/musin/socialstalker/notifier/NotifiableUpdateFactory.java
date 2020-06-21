package musin.socialstalker.notifier;

import musin.socialstalker.db.model.RelationUpdate;
import musin.socialstalker.relation.User;

public interface NotifiableUpdateFactory<
    TUser extends User<?>,
    TRelationType,
    TNotifiableUpdate extends NotifiableUpdate<TUser, TRelationType>> {
  TNotifiableUpdate create(RelationUpdate relationUpdate);
}