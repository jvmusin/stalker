package musin.socialstalker.notifier;

import java.util.List;

public interface UpdateNotifier<TRelationType> {
  void notify(NotifiableUpdate<TRelationType> update);

  default void notify(List<NotifiableUpdate<TRelationType>> updates) {
    updates.forEach(this::notify);
  }
}
