package pet.project.exception;

public class NoCompanyFoundException extends RuntimeException {

  public NoCompanyFoundException(Integer companyId) {
    super("Company id = %s not found".formatted(companyId));
  }
}
