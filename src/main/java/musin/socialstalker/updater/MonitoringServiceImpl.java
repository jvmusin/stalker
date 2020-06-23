package musin.socialstalker.updater;

import lombok.RequiredArgsConstructor;
import musin.socialstalker.db.model.Stalker;

import java.util.List;

@RequiredArgsConstructor
public class MonitoringServiceImpl<ID> implements MonitoringService<ID> {

  private final Stalker stalker;
  private final GeneralMonitoringService<ID> generalMonitoringService;

  @Override
  public List<? extends ID> findAllTargets() {
    return generalMonitoringService.findAllTargets(stalker);
  }

  @Override
  public boolean createMonitoring(ID targetId) {
    return generalMonitoringService.createMonitoring(stalker, targetId);
  }

  @Override
  public boolean deleteMonitoring(ID targetId) {
    return generalMonitoringService.deleteMonitoring(stalker, targetId);
  }
}
