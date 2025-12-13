package jp.ne.yonem.restful.presentation.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jp.ne.yonem.restful.infrastructure.exception.BusinessRuleViolationException;
import jp.ne.yonem.restful.infrastructure.validation.MessageResolverStrategy;
import jp.ne.yonem.restful.presentation.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
  private final MessageUtil messageUtil;
  private final List<MessageResolverStrategy> messageResolvers;

  @ExceptionHandler(BusinessRuleViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST) // 例: HTTP 400 を返す
  public ResponseEntity<Map<String, MessageResponse>> handleBusinessViolation(
      BusinessRuleViolationException ex) {
    log.error(
        "Business rule violation. Key: {}, Args: {}",
        ex.getMessageKey(),
        Arrays.toString(ex.getMessageArgs()),
        ex);

    // 2. 外部応答の生成
    var message =
        messageUtil.getResponse(ex.getMessageKey(), ex.getMessageArgs(), MDC.get("trace_id"));
    return new ResponseEntity<>(Map.of("error", message), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<MessageResponse>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    var result = ex.getBindingResult();

    var errors =
        result.getAllErrors().stream()
            .map(
                error -> {
                  var messageId =
                      Objects.nonNull(error.getDefaultMessage())
                          ? error.getDefaultMessage()
                          : "E999";
                  var message = "不明なメッセージ";
                  var fieldError = (error instanceof FieldError) ? (FieldError) error : null;
                  var target = result.getTarget();
                  var resolver =
                      messageResolvers.stream()
                          .filter(s -> s.supports(messageId))
                          .findFirst()
                          .orElse(null);

                  if (Objects.nonNull(resolver)) {
                    message = resolver.resolveMessage(messageId, target, fieldError);
                  }
                  return new MessageResponse(messageId, message);
                })
            .toList();
    return new ResponseEntity<>(Map.of("error", errors), HttpStatus.BAD_REQUEST);
  }
}
