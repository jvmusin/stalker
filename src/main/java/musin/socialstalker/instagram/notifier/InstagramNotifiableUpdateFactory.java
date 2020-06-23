package musin.socialstalker.instagram.notifier;

import musin.socialstalker.db.model.RelationUpdate;
import musin.socialstalker.instagram.api.InstagramID;
import musin.socialstalker.instagram.api.InstagramIdFactory;
import musin.socialstalker.instagram.config.InstagramNetworkProperties;
import musin.socialstalker.instagram.relation.InstagramRelationType;
import musin.socialstalker.instagram.relation.InstagramRelationTypeFactory;
import musin.socialstalker.instagram.relation.InstagramUserFactory;
import musin.socialstalker.notifier.NotifiableUpdateFactoryBase;
import musin.socialstalker.relation.RelationType;
import org.springframework.stereotype.Component;

@Component
public class InstagramNotifiableUpdateFactory extends NotifiableUpdateFactoryBase<InstagramID, RelationType> {

  public InstagramNotifiableUpdateFactory(InstagramUserFactory userFactory,
                                          InstagramRelationTypeFactory relationTypeFactory,
                                          InstagramNetworkProperties networkProperties,
                                          InstagramIdFactory idFactory) {
    super(userFactory, relationTypeFactory, networkProperties, idFactory);
  }

  @Override
  public InstagramNotifiableUpdate create(RelationUpdate update) {
    return new InstagramNotifiableUpdateImpl(update);
  }

  private class InstagramNotifiableUpdateImpl extends NotifiableUpdateBase implements InstagramNotifiableUpdate {
    protected InstagramNotifiableUpdateImpl(RelationUpdate update) {
      super(update);
    }
  }
}
