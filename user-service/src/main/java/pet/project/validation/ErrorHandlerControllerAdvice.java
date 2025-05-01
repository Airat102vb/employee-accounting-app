package pet.project.validation;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pet.project.exception.NoEmployeeFoundException;

@RestControllerAdvice
public class ErrorHandlerControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<Violation> onConstraintValidationException(
      ConstraintViolationException constraintViolation) {
    return constraintViolation.getConstraintViolations().stream()
        .map(
            violation ->
                new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
        .collect(Collectors.toList());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<Violation> onMethodArgumentNotValidException(
      MethodArgumentNotValidException methodArgumentNotValid) {
    return methodArgumentNotValid.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
        .toList();
  }

  @ExceptionHandler(NoEmployeeFoundException.class)
  public ResponseEntity<String> onNoEmployeeFoundException(NoEmployeeFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
