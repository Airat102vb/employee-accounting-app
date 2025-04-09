package pet.project.controller;

import jakarta.ws.rs.QueryParam;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pet.project.CompanyService;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;

@RestController
public class CompanyController {

  private CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping("/add")
  public ResponseEntity addCompany(@RequestBody CompanyDto newCompany) {
    Integer newCompanyId = companyService.insertCompany(newCompany);
    return Objects.nonNull(newCompanyId)
        ? ResponseEntity.status(HttpStatus.CREATED)
            .body("Создана компания с id = %s".formatted(newCompanyId))
        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось добавить компанию");
  }

  @PostMapping("/add-employee")
  public ResponseEntity addEmployee(
      @QueryParam(value = "companyId") Integer companyId,
      @QueryParam(value = "employeeId") Integer employeeId) {
    int result = companyService.insertEmployeeToCompany(companyId, employeeId);
    return result == 0
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Не удалось добавить пользователя в компанию")
        : ResponseEntity.status(HttpStatus.CREATED).body("Пользователь добавлен в компанию");
  }

  @GetMapping("/get")
  public ResponseEntity<CompanyWithUsersDto> getCompany(
      @QueryParam(value = "companyId") String companyId) {
    CompanyWithUsersDto company = companyService.getCompany(companyId);
    return ResponseEntity.ok(company);
  }

  @GetMapping("/get-by-user")
  public ResponseEntity getCompanyByUser(@QueryParam(value = "employeeId") String employeeId) {
    CompanyDto company = companyService.getCompanyByUserId(employeeId);
    return ResponseEntity.ok(company);
  }

  @PutMapping("/update")
  public ResponseEntity updateCompany(@RequestBody CompanyDto newCompanyData) {
    companyService.updateCompany(newCompanyData);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity deleteCompany(@QueryParam(value = "companyId") String companyId) {
    companyService.deleteCompany(companyId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/all")
  public ResponseEntity getAllCompanies() {
    return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompanies());
  }
}
