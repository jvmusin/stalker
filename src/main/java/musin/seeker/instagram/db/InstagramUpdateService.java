package musin.seeker.instagram.db;

import musin.seeker.db.update.RelationUpdate;
import musin.seeker.db.update.RelationUpdateRepository;
import musin.seeker.instagram.api.InstagramID;
import musin.seeker.instagram.notifier.InstagramNotifiableUpdate;
import musin.seeker.instagram.relation.*;
import musin.seeker.updater.UpdateServiceBase;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("instagram")
public class InstagramUpdateService extends UpdateServiceBase<InstagramID, InstagramUser, InstagramRelationType, InstagramUpdate, InstagramRelationList, InstagramNotifiableUpdate> {

  public InstagramUpdateService(RelationUpdateRepository relationUpdateRepository, InstagramUserFactory instagramUserFactory) {
    super(relationUpdateRepository, instagramUserFactory);
  }

  @Override
  protected InstagramNotifiableUpdate createNotifiableUpdate(RelationUpdate update) {
    return new InstagramNotifiableUpdateImpl(update);
  }

  @Override
  protected InstagramRelationList createList(List<InstagramNotifiableUpdate> updates) {
    return new InstagramRelationList(updates);
  }

  @Override
  protected String getResource() {
    return InstagramConstants.RESOURCE;
  }

  private class InstagramNotifiableUpdateImpl extends NotifiableUpdateBase implements InstagramNotifiableUpdate {
    protected InstagramNotifiableUpdateImpl(RelationUpdate update) {
      super(update);
    }

    @Override
    protected InstagramID parseId(String id) {
      return new InstagramID(id);
    }

    @Override
    protected InstagramRelationType parseRelationType(String type) {
      return InstagramRelationType.parseNullSafe(type);
    }
  }
}
