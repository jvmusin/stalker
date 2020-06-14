package musin.seeker.notifier;

import java.util.List;

public interface UpdateNotifier<TUpdate> {
  void notify(TUpdate update);

  default void notify(List<? extends TUpdate> updates) {
    updates.forEach(this::notify);
  }
}