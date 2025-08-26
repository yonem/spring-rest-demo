package jp.ne.yonem.restful.demo.validation;

import jakarta.validation.metadata.ConstraintDescriptor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jp.ne.yonem.restful.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    var result = ex.getBindingResult();
    var errors = new ArrayList<ErrorResponse>();

    for (var error : result.getAllErrors()) {
      if (error instanceof FieldError fieldError) {
        if (Objects.nonNull(fieldError.getDefaultMessage())) {
          var msg = ErrorMessages.of(fieldError.getDefaultMessage());
          var descriptor = fieldError.unwrap(ConstraintDescriptor.class);
          errors.add(
              new ErrorResponse(
                  msg.getMessageId(),
                  MessageFormat.format(msg.getMessage(), descriptor.getAttributes().values())));
        }
      }
    }
    return errors;
  }
}
