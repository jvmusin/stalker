package musin.seeker.instagram.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserInfoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
//todo add restriction to 200 requests per hour
public class InstagramApiImpl implements InstagramApi {

  private final Instagram4j instagram;
  private final Map<Long, InstagramApiUser> users = new ConcurrentHashMap<>();

  private InstagramApiUser mapUser(InstagramUserSummary user) {
    return new InstagramApiUser(user.pk, user.username);
  }

  private InstagramApiUser mapUser(InstagramUser user) {
    return new InstagramApiUser(user.pk, user.username);
  }

  private void saveUser(long pk, InstagramApiUser user) {
    users.put(pk, user);
  }

  @Override
  public InstagramApiUser loadUser(long userId) {
    //noinspection Convert2Lambda
    return users.computeIfAbsent(userId, new Function<>() {
      @Override
      @SneakyThrows
      public InstagramApiUser apply(Long id) {
        InstagramSearchUsernameResult res = instagram.sendRequest(new InstagramGetUserInfoRequest(id));
        return InstagramApiImpl.this.mapUser(res.getUser());
      }
    });
  }

  @Override
  @SneakyThrows
  public CompletableFuture<List<Long>> loadFollowers(long userId) {
    InstagramGetUserFollowersResult githubFollowers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userId));
    List<InstagramUserSummary> followers = githubFollowers.getUsers();
    followers.forEach(u -> saveUser(u.pk, mapUser(u)));
    List<Long> result = followers.stream().map(u -> u.pk).collect(toList());
    return completedFuture(result);
  }
}