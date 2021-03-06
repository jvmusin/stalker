package musin.socialstalker.db.service;

import lombok.RequiredArgsConstructor;
import musin.socialstalker.db.model.Stalker;
import musin.socialstalker.db.repository.StalkerRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class StalkerService {
  private final StalkerRepository repository;

  @Transactional
  public Stalker get(long telegramId) {
    return repository.findByTelegramId(telegramId)
        .orElseGet(() -> repository.save(new Stalker(null, telegramId)));
  }
}
