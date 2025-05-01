package pet.project.exception;

public class NoEmployeeFoundException extends RuntimeException {

  public NoEmployeeFoundException(Integer employeeId) {
    super("Employee id = %s not found".formatted(employeeId));
  }
}
