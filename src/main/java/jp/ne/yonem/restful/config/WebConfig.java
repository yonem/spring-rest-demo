package jp.ne.yonem.restful.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final MdcTraceIdInterceptor mdcTraceIdInterceptor;

  @Autowired
  public WebConfig(MdcTraceIdInterceptor mdcTraceIdInterceptor) {
    this.mdcTraceIdInterceptor = mdcTraceIdInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    // すべてのパス ("/**") に対してインターセプターを適用
    registry.addInterceptor(mdcTraceIdInterceptor);
  }
}
