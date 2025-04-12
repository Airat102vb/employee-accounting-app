package pet.project;

import static pet.project.mapper.CompanyMapper.mapToCompany;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.project.clients.UserServiceClient;
import pet.project.dao.CompanyRepositoryHibernate;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;
import pet.project.entity.Company;

@Service
public class CompanyService {

  private final Logger logger = LoggerFactory.getLogger("CompanyService");
  private UserServiceClient userServiceClient;
  private CompanyRepositoryHibernate companyRepositoryHibernate;

  @Autowired
  public CompanyService(
      CompanyRepositoryHibernate companyRepositoryHibernate, UserServiceClient userServiceClient) {
    this.companyRepositoryHibernate = companyRepositoryHibernate;
    this.userServiceClient = userServiceClient;
  }

  public Integer addCompany(CompanyDto newCompany) {
    Company newCreatedCompany = companyRepositoryHibernate.save(mapToCompany(newCompany));
    return newCreatedCompany.getId();
  }

  public void insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();
    company.getEmployeeIds().add(employeeId);
    companyRepositoryHibernate.save(company);
  }

  public CompanyWithUsersDto getCompany(Integer companyId, boolean withUserInfo) {
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();

    if (withUserInfo) {
      List<UserDto> employees =
          company.getEmployeeIds().stream()
              .map(id -> userServiceClient.getUser(id, true).getBody())
              .map(
                  dto ->
                      new UserDto(
                          dto.id(), dto.firstName(), dto.firstName(), dto.phoneNumber(), companyId))
              .toList();

      return new CompanyWithUsersDto(
          company.getId(), company.getCompanyName(), company.getBudget(), employees);
    }

    return new CompanyWithUsersDto(
        company.getId(), company.getCompanyName(), company.getBudget(), null);
  }

  public void updateCompany(Integer companyId, CompanyDto newCompanyData) {
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();

    company.setCompanyName(newCompanyData.companyName());
    company.setBudget(newCompanyData.budget());
    company.setEmployeeIds(newCompanyData.employeeId());

    companyRepositoryHibernate.save(company);
  }

  public void deleteCompany(Integer companyId) {
    companyRepositoryHibernate.deleteById(companyId);
  }

  public List<CompanyWithUsersDto> getAllCompanies() {
    List<CompanyWithUsersDto> resultDto = new LinkedList<>();
    List<Company> companies = companyRepositoryHibernate.findAll();

    for (Company company : companies) {
      if (Objects.nonNull(company.getEmployeeIds()) && !company.getEmployeeIds().isEmpty()) {
        List<UserDto> user =
            company.getEmployeeIds().stream()
                .map(empId -> userServiceClient.getUser(empId, false).getBody())
                .map(
                    dto ->
                        new UserDto(
                            dto.id(),
                            dto.firstName(),
                            dto.lastName(),
                            dto.phoneNumber(),
                            company.getId()))
                .toList();
        resultDto.add(
            new CompanyWithUsersDto(
                company.getId(), company.getCompanyName(), company.getBudget(), user));
      }
    }
    return resultDto;
  }
}
