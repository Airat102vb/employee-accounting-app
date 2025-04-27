package pet.project.service;

import static pet.project.mapper.CompanyMapper.mapToCompany;
import static pet.project.mapper.CompanyMapper.mapToUserWithCompanyDto;

import jakarta.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pet.project.clients.UserServiceClient;
import pet.project.dao.CompanyRepositoryHibernate;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;
import pet.project.entity.Company;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

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
    log.info("Добавление компании: {}", newCompany);
    return companyRepositoryHibernate.save(mapToCompany(newCompany));
  }

  @Override
  public void insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    log.info("Добавление сотрудника {} в компанию {}", employeeId, companyId);
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();
    company.getEmployeeIds().add(employeeId);
    companyRepositoryHibernate.save(company);
  }

  @Override
  public CompanyWithUsersDto getCompany(Integer companyId, boolean withUserInfo) {
    log.info("Получение компании: {}", companyId);
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
    log.info("Обновление компании: {} данными {}", companyId, newCompanyData);
    Company company = companyRepositoryHibernate.findById(companyId).orElseThrow();
    Company companyData = mapToCompany(newCompanyData);
    companyData.setId(company.getId());
    companyRepositoryHibernate.save(company);
  }

  @Override
  public void deleteCompany(Integer companyId) {
    log.info("Удаление компании: {}", companyId);
    companyRepositoryHibernate.deleteById(companyId);
  }

  @Override
  public PageImpl<CompanyWithUsersDto> getCompanies(int pageNumber, int pageSize) {
    log.info("Получение списка компаний: pageNumber {}; pageSize {}", pageNumber, pageSize);
    List<CompanyWithUsersDto> resultDto = new LinkedList<>();
    Page<Company> companies =
        companyRepositoryHibernate.findAll(PageRequest.of(pageNumber, pageSize));

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
    return new PageImpl(
        resultDto, PageRequest.of(pageNumber, pageSize), companies.getTotalElements());
  }
}
