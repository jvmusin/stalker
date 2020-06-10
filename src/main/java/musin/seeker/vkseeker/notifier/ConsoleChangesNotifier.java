package musin.seeker.vkseeker.notifier;

import lombok.extern.log4j.Log4j2;
import musin.seeker.vkseeker.vk.VkApi;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ConsoleChangesNotifier extends ChangesNotifierBase {

  public ConsoleChangesNotifier(VkApi vkApi) {
    super(vkApi);
  }

  @Override
  protected void sendMessage(String message) {
    log.info(message);
  }
}