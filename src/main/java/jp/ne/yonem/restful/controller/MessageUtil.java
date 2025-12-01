package jp.ne.yonem.restful.controller;

import jp.ne.yonem.restful.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageUtil {
  private final MessageSource messageSource;

  public String getMessage(String id) {
    return getMessage(id, new Object[0], LocaleContextHolder.getLocale());
  }

  public String getMessage(String id, Object... arg) {
    return messageSource.getMessage(id, arg, LocaleContextHolder.getLocale());
  }

  public MessageResponse getResponse(String id) {
    return getResponse(id, new Object[0]);
  }

  public MessageResponse getResponse(String id, Object... arg) {
    return new MessageResponse(id, getMessage(id, arg));
  }
}
