package pet.project.service;

import static pet.project.mapper.CompanyMapper.mapToCompany;
import static pet.project.mapper.CompanyMapper.mapToUserWithCompanyDto;

import jakarta.validation.Valid;
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
public class CompanyServiceImpl implements CompanyService {

  private final Logger logger = LoggerFactory.getLogger("CompanyService");
  private UserServiceClient userServiceClient;
  private CompanyRepositoryHibernate companyRepositoryHibernate;

  @Autowired
  public CompanyServiceImpl(
      CompanyRepositoryHibernate companyRepositoryHibernate, UserServiceClient userServiceClient) {
    this.companyRepositoryHibernate = companyRepositoryHibernate;
    this.userServiceClient = userServiceClient;
  }

  @Override
  public Company addCompany(@Valid CompanyDto newCompany) {
    return companyRepositoryHibernate.save(mapToCompany(newCompany));
  }

  @Override
  public void insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();
    company.getEmployeeIds().add(employeeId);
    companyRepositoryHibernate.save(company);
  }

  @Override
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

      return mapToUserWithCompanyDto(company, employees);
    }

    return mapToUserWithCompanyDto(company, null);
  }

  @Override
  public void updateCompany(Integer companyId, @Valid CompanyDto newCompanyData) {
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();
    Company companyData = mapToCompany(newCompanyData);
    companyData.setId(company.getId());
    companyRepositoryHibernate.save(company);
  }

  @Override
  public void deleteCompany(Integer companyId) {
    companyRepositoryHibernate.deleteById(companyId);
  }

  @Override
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
        resultDto.add(mapToUserWithCompanyDto(company, user));
      }
    }
    return resultDto;
  }
}
