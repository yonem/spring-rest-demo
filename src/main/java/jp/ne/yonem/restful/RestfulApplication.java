package jp.ne.yonem.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// @SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "jp.ne.yonem.restful")
// @EnableScheduling
public class RestfulApplication {
  public static void main(String[] args) {
    SpringApplication.run(RestfulApplication.class, args);
  }
}
