package jp.ne.yonem.restful.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MdcTraceIdInterceptor implements HandlerInterceptor {
  private static final String TRACE_ID_KEY = "trace_id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    // MDC に trace_id を格納 (リクエスト内のすべてのログに自動で付与される)
    MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString());
    return true;
  }

  /** リクエストが完了した後、必ずMDC情報を削除する。 これを怠ると、スレッドプールが再利用された際に古いMDC情報が誤って使用される。 */
  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      @Nullable Exception ex)
      throws Exception {

    // MDC から格納したキーを削除
    // MDC.clear() は全てのキーを削除するが、他のライブラリがMDCを使用している場合はMDC.remove()の方が安全
    MDC.remove(TRACE_ID_KEY);
  }
}
