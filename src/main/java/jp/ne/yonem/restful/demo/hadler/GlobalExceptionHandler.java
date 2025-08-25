package jp.ne.yonem.restful.demo.hadler;

import static jp.ne.yonem.restful.demo.hadler.ErrorMessages.*;

import java.text.MessageFormat;
import java.util.Objects;
import jp.ne.yonem.restful.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    var fieldError = ex.getBindingResult().getFieldError();

    if (Objects.nonNull(fieldError) && Objects.nonNull(fieldError.getDefaultMessage())) {
      var msg = ErrorMessages.of(fieldError.getDefaultMessage());
      return new ErrorResponse(
          msg.getMessageId(), MessageFormat.format(msg.getMessage(), fieldError.getField()));
    }
    return new ErrorResponse(W100100.getMessageId(), W100100.getMessage());
  }
}
