package pet.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.QueryParam;
import java.net.URI;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.entity.Company;
import pet.project.service.CompanyService;

@Slf4j
@Validated
@RestController
@RequestMapping("/company")
public class CompanyController {

  private CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping
  public ResponseEntity addCompany(@Valid @RequestBody CompanyDto newCompany) {
    log.info("Добавление компании: {}", newCompany);
    Company company = companyService.addCompany(newCompany);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(company.getId())
            .toUri();
    return ResponseEntity.created(uri).body(company);
  }

  @PostMapping("/user/{id}")
  public ResponseEntity addEmployee(
      @PathVariable(value = "id") @Min(1) Integer companyId,
      @QueryParam(value = "employeeId") @Min(1) Integer employeeId) {
    log.info("Добавление сотрудника: {}, в компанию {}", employeeId, companyId);
    companyService.insertEmployeeToCompany(companyId, employeeId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<CompanyWithUsersDto> getCompany(
      @PathVariable(value = "id") @Min(1) Integer companyId,
      @RequestParam(value = "withUserInfo", defaultValue = "false") boolean withUserInfo) {
    log.info("Получение компании: {}, с флагом withUserInfo {}", companyId, withUserInfo);
    CompanyWithUsersDto company = companyService.getCompany(companyId, withUserInfo);
    return ResponseEntity.ok(company);
  }

  @PutMapping("{id}")
  public ResponseEntity updateCompany(
      @PathVariable(value = "id") @Min(1) Integer companyId,
      @Valid @RequestBody CompanyDto newCompanyData) {
    log.info("Обновление компании: {}, новыми данными {}", companyId, newCompanyData);
    companyService.updateCompany(companyId, newCompanyData);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteCompany(@PathVariable(value = "id") @Min(1) Integer companyId) {
    log.info("Удаление компании: {}", companyId);
    companyService.deleteCompany(companyId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping
  public ResponseEntity getCompanies(
      @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    log.info(
        "Получение списка компаний со станицы {}, с количеством компаний на странице {}",
        pageNumber,
        pageSize);
    return ResponseEntity.status(HttpStatus.OK)
        .body(companyService.getCompanies(pageNumber, pageSize));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> onNoSuchElementException(NoSuchElementException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
  }
}
