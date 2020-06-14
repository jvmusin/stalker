package musin.seeker.vk.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import musin.seeker.updater.ScheduledUpdaterRule;
import musin.seeker.vk.updater.VkUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkConfig {

  @Bean
  public VkApiClient vkApiClient(TransportClient transportClient) {
    return new VkApiClient(transportClient);
  }

  @Bean
  public UserActor defaultUserActor(VkConfigurationProperties config) {
    return new UserActor(config.getUserId(), config.getUserAccessToken());
  }

  @Bean
  public ScheduledUpdaterRule vkScheduledUpdaterRule(VkUpdater updater, VkConfigurationProperties config) {
    return new VkScheduledUpdaterRule(updater, config.getPeriodBetweenUpdates());
  }
}