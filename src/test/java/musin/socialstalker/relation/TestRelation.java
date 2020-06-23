package musin.socialstalker.relation;

import lombok.Data;

@Data
public class TestRelation implements Relation<User<?>, TestRelationType> {
  private final User<?> user;
  private final TestRelationType type;

  TestRelation(String user, String type) {
    this.user = new TestUser(user);
    this.type = new TestRelationType(type);
  }

  public TestRelation(User<?> user, TestRelationType type) {
    this.user = user;
    this.type = type;
  }

  TestUpdate asAdd() {
    return new TestUpdate(user, null, type);
  }

  TestUpdate asRemove() {
    return new TestUpdate(user, type, null);
  }
}
