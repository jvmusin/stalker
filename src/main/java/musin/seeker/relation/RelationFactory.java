package musin.seeker.relation;

public interface RelationFactory<TUser, TRelationType, TRelation> {
  TRelation create(TUser user, TRelationType type);
}
