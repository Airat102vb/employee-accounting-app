package pet.project.controller;

import jakarta.ws.rs.QueryParam;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/company")
public class CompanyController {

  private CompanyService companyService;

  @Autowired
  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @PostMapping
  public ResponseEntity addCompany(@RequestBody CompanyDto newCompany) {
    Company company = companyService.addCompany(newCompany);
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(company.getId())
        .toUri();
    return ResponseEntity.created(uri).body(company);
  }

  @PostMapping("/user/{id}")
  public ResponseEntity addEmployee(
      @PathVariable(value = "id") Integer companyId,
      @QueryParam(value = "employeeId") Integer employeeId) {
    companyService.insertEmployeeToCompany(companyId, employeeId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<CompanyWithUsersDto> getCompany(
      @PathVariable(value = "id") Integer companyId,
      @RequestParam(value = "withUserInfo", defaultValue = "false") boolean withUserInfo) {
    CompanyWithUsersDto company = companyService.getCompany(companyId, withUserInfo);
    return ResponseEntity.ok(company);
  }

  @PutMapping("{id}")
  public ResponseEntity updateCompany(
      @PathVariable(value = "id") Integer companyId, @RequestBody CompanyDto newCompanyData) {
    companyService.updateCompany(companyId, newCompanyData);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteCompany(@PathVariable(value = "id") Integer companyId) {
    companyService.deleteCompany(companyId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping
  public ResponseEntity getAllCompanies() {
    return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompanies());
  }
}
