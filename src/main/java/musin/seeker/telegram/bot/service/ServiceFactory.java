package musin.seeker.telegram.bot.service;

import musin.seeker.db.model.Stalker;

public interface ServiceFactory {
  Service create(Stalker stalker);
}
