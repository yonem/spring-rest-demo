package jp.ne.yonem.restful.infrastructure.batch;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CronJob {

  @Value("${batch.cron.expression}")
  private String cronExpression;

  @Scheduled(cron = "${batch.cron.expression}")
  public void runBatch() {
    log.info("バッチ処理が実行されました: {}", LocalDateTime.now());
    log.info("設定されたCron式: {}", cronExpression);
  }
}
