package jp.ne.yonem.restful.demo.validation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import jp.ne.yonem.restful.demo.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final List<MessageResolverStrategy> messageResolvers;

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
