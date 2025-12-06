package jp.ne.yonem.restful.infrastructure.batch;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArgsRunner implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) {
    log.info("ArgsRunner が実行されました");

    // 生の引数を取得
    log.info("生の引数: {}", List.of(args.getSourceArgs()));

    // オプション引数名を取得
    var optionNames = args.getOptionNames();
    log.info("オプション引数名: {}", optionNames);

    // 各オプション引数の値を取得
    for (var optionName : optionNames) {
      var values = args.getOptionValues(optionName);
      log.info("  {}: {}", optionName, values);
    }

    // 非オプション引数を取得
    var nonOptionArgs = args.getNonOptionArgs();
    log.info("非オプション引数: {}", nonOptionArgs);
  }
}
