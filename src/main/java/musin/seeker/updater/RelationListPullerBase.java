package musin.seeker.updater;

import lombok.RequiredArgsConstructor;
import musin.seeker.relation.*;
import musin.seeker.relation.list.RelationList;
import musin.seeker.relation.list.RelationListFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Stream.empty;

@RequiredArgsConstructor
public abstract class RelationListPullerBase<
    ID,
    TUser,
    TRelationType,
    TRelation extends Relation<TUser, TRelationType>,
    TUpdate extends Update<TUser, TRelationType>,
    TRelationList extends RelationList<TUser, TRelationType>>
    implements RelationListPuller<ID, TRelationList> {

  private final RelationListFactory<TRelationList> relationListFactory;
  private final UpdateFactory<TUser, TRelationType, TUpdate> updateFactory;
  private final RelationFactory<TUser, TRelationType, TRelation> relationFactory;
  private final UserFactory<ID, TUser> userFactory;
  private final List<Query> queries = new ArrayList<>();

  protected void registerQuery(Function<ID, CompletableFuture<List<ID>>> query, TRelationType resultType) {
    queries.add(new Query(query, resultType));
  }

  @Override
  public CompletableFuture<TRelationList> pull(ID userId) {
    return queries.stream()
        .map(q -> q.call(userId))
        .reduce(completedFuture(empty()), (a, b) -> a.thenCombine(b, Stream::concat))
        .thenApply(relations -> {
          TRelationList list = relationListFactory.create();
          relations.forEach(r -> list.apply(updateFactory.creating(r.getUser(), r.getType())));
          return list;
        });
  }

  @RequiredArgsConstructor
  private class Query {
    final Function<ID, CompletableFuture<List<ID>>> query;
    final TRelationType type;

    CompletableFuture<Stream<TRelation>> call(ID userId) {
      return query.apply(userId).thenApply(list -> list.stream().map(id -> relationFactory.create(userFactory.create(id), type)));
    }
  }
}
