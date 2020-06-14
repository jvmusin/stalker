package musin.seeker.relation;

import lombok.Data;

@Data
public class TestUser implements User {
  private final String name;

  @Override
  public String getLink() {
    return "link_to_user_" + name;
  }
}