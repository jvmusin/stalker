package musin.socialstalker.instagram.relation;

import lombok.RequiredArgsConstructor;
import musin.socialstalker.instagram.api.InstagramApi;
import musin.socialstalker.instagram.api.InstagramApiUser;
import musin.socialstalker.instagram.api.InstagramID;
import musin.socialstalker.relation.LazyLoadingUser;
import musin.socialstalker.relation.UserFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Profile("instagram")
@RequiredArgsConstructor
public class InstagramUserFactory implements UserFactory<InstagramID, InstagramUser> {
  private final InstagramApi instagramApi;

  @Override
  public InstagramUser create(InstagramID id) {
    return new InstagramUserImpl(id, instagramApi::getUserInfo);
  }

  private static class InstagramUserImpl extends LazyLoadingUser<InstagramID, InstagramApiUser> implements InstagramUser {
    InstagramUserImpl(InstagramID instagramID, Function<InstagramID, InstagramApiUser> loadUser) {
      super(instagramID, loadUser);
    }

    @Override
    public String getFullyQualifiedName() {
      return String.format("%s: %s (%s)", user().getId(), user().getFullName(), user().getUsername());
    }

    @Override
    public String getLink() {
      return "https://instagram.com/" + user().getUsername();
    }
  }
}