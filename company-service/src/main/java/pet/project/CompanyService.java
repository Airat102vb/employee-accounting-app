package pet.project;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pet.project.clients.UserServiceClient;
import pet.project.dao.CompanyRepository;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyInfoDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;

@Service
public class CompanyService {

  private final Logger logger = LoggerFactory.getLogger("CompanyService");
  private CompanyRepository companyRepository;
  private UserServiceClient userServiceClient;

  @Autowired
  public CompanyService(CompanyRepository companyRepository, UserServiceClient userServiceClient) {
    this.companyRepository = companyRepository;
    this.userServiceClient = userServiceClient;
  }

  public Integer insertCompany(CompanyDto newCompany) {
    Integer companyId = companyRepository.insertCompany(newCompany);
    if (Objects.nonNull(companyId)) {
      if (Objects.nonNull(newCompany.employeeId()) && !newCompany.employeeId().isEmpty()) {
        newCompany
            .employeeId()
            .forEach(employeeId -> insertEmployeeToCompany(companyId, employeeId));
      }
    }
    return companyId;
  }

  public Integer insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    return companyRepository.insertEmployeeToCompany(companyId, employeeId);
  }

  public CompanyWithUsersDto getCompany(String companyId) {
    CompanyInfoDto companyDto = companyRepository.getCompanyById(companyId);
    if (companyDto == null) {
      return new CompanyWithUsersDto(null, null, null, null);
    }

    List<Integer> employeeIds =
        companyRepository.getEmployeeIdsOfCompany(companyDto.id().toString());

    List<UserDto> employees =
        employeeIds.stream()
            .map(
                id -> {
                  ResponseEntity<UserDto> response = userServiceClient.getUser(String.valueOf(id));
                  return response.getBody();
                })
            .toList();

    return new CompanyWithUsersDto(
        companyDto.id(), companyDto.companyName(), companyDto.budget(), employees);
  }

  public CompanyDto getCompanyByUserId(String employeeId) {
    return companyRepository.getCompanyByOfUserById(employeeId);
  }

  public void updateCompany(CompanyDto newCompanyData) {
    companyRepository.updateCompany(newCompanyData);
    if (Objects.nonNull(newCompanyData.employeeId()) && !newCompanyData.employeeId().isEmpty()) {
      companyRepository.deleteEmployees(newCompanyData.id());
      companyRepository.updateEmployees(newCompanyData);
    }
  }

  public void deleteCompany(String companyId) {
    if (companyRepository.deleteEmployees(Integer.parseInt(companyId))) {
      logger.info("Сотрудники из компании {} успешно удалены", companyId);
    } else {
      logger.warn("В компании с id {} не было сотрудников", companyId);
    }

    if (companyRepository.deleteCompanyById(companyId)) {
      logger.info("Компания с id {} успешно удалена", companyId);
    } else {
      logger.warn("Компания с id {} не найдена", companyId);
    }
  }

  public List<CompanyWithUsersDto> getAllCompanies() {
    List<CompanyWithUsersDto> companies = new LinkedList<>();
    List<CompanyInfoDto> companiesInfo = companyRepository.getAllCompanies();

    companiesInfo.forEach(
        companyInfo -> {
          List<Integer> employeeIds =
              companyRepository.getEmployeeIdsOfCompany(companyInfo.id().toString());
          List<UserDto> employees =
              employeeIds.stream()
                  .map(
                      id -> {
                        ResponseEntity<UserDto> response =
                            userServiceClient.getUser(String.valueOf(id));
                        return response.getBody();
                      })
                  .toList();
          companies.add(
              new CompanyWithUsersDto(
                  companyInfo.id(), companyInfo.companyName(), companyInfo.budget(), employees));
        });
    return companies;
  }
}
