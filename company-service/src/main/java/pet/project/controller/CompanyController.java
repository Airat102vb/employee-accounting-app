package pet.project.controller;

import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pet.project.dao.CompanyRepository;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;

@RestController
public class CompanyController {

  private CompanyRepository companyRepository;

  @Autowired
  public CompanyController(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  @PostMapping("/add")
  public ResponseEntity addCompany(@RequestBody CompanyDto newCompany) {
    int result = companyRepository.insertCompany(newCompany);
    return result == 0
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось добавить компанию")
        : ResponseEntity.status(HttpStatus.CREATED)
            .body("Создана компания с id = %s".formatted(result));
  }

  @PostMapping("/add-employee")
  public ResponseEntity addEmployee(
      @QueryParam(value = "companyId") Integer companyId,
      @QueryParam(value = "employeeId") Integer employeeId) {
    int result = companyRepository.insertEmployeeToCompany(companyId, employeeId);
    return result == 0
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Не удалось добавить пользователя в компанию")
        : ResponseEntity.status(HttpStatus.CREATED).body("Пользователь добавлен в компанию");
  }

  @GetMapping("/get")
  public ResponseEntity getCompany(@QueryParam(value = "companyId") String companyId) {
    CompanyWithUsersDto company = companyRepository.getCompanyById(companyId);
    return ResponseEntity.ok(company);
  }

  @GetMapping("/get-by-user")
  public ResponseEntity getCompanyByUser(@QueryParam(value = "employeeId") String employeeId) {
    CompanyDto company = companyRepository.getCompanyByUserId(employeeId);
    return ResponseEntity.ok(company);
  }

  @PutMapping("/update")
  public ResponseEntity updateCompany(@RequestBody CompanyDto newCompanyData) {
    companyRepository.update(newCompanyData);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity deleteCompany(@QueryParam(value = "companyId") String companyId) {
    companyRepository.deleteCompany(companyId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/all")
  public ResponseEntity getAllCompanies() {
    return ResponseEntity.status(HttpStatus.OK).body(companyRepository.getAllCompanies());
  }
}
