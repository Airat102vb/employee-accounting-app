package pet.project.service;

import static pet.project.mapper.CompanyMapper.mapToCompany;
import static pet.project.mapper.CompanyMapper.mapToUserWithCompanyDto;

import jakarta.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import pet.project.exception.NoCompanyFoundException;

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
    Company company = companyRepositoryHibernate.save(mapToCompany(newCompany));
    log.info("Компания успешно добавлена: {}", company);
    return company;
  }

  @Override
  public void insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    Company company =
        companyRepositoryHibernate
            .findById(companyId)
            .orElseThrow(() -> new NoCompanyFoundException(companyId));
    company.getEmployeeIds().add(employeeId);
    companyRepositoryHibernate.save(company);
    log.info("Сотрудник id = {} успешно добавлен в компанию {}", employeeId, company);
  }

  @Override
  public CompanyWithUsersDto getCompany(Integer companyId, boolean withUserInfo) {
    CompanyWithUsersDto companyWithUsersDto;
    Company company =
        companyRepositoryHibernate
            .findById(companyId)
            .orElseThrow(() -> new NoCompanyFoundException(companyId));

    if (withUserInfo) {
      String ids =
          company.getEmployeeIds().stream().map(String::valueOf).collect(Collectors.joining(","));
      List<UserDto> users = userServiceClient.getUsers(ids).getBody();
      companyWithUsersDto = mapToUserWithCompanyDto(company, users);
      log.info("Компания успешно получена: {}", companyWithUsersDto);
      return companyWithUsersDto;
    }

    companyWithUsersDto = mapToUserWithCompanyDto(company, null);
    log.info("Компания успешно получена: {}", companyWithUsersDto);
    return companyWithUsersDto;
  }

  @Override
  public void updateCompany(Integer companyId, @Valid CompanyDto newCompanyData) {
    Company company =
        companyRepositoryHibernate
            .findById(companyId)
            .orElseThrow(() -> new NoCompanyFoundException(companyId));
    Company companyData = mapToCompany(newCompanyData);
    companyData.setId(company.getId());
    Company updated = companyRepositoryHibernate.save(companyData);
    log.info("Компании успешно обновлена: {}", updated);
  }

  @Override
  public void deleteCompany(Integer companyId) {
    companyRepositoryHibernate.deleteById(companyId);
    log.info("Компания удалена: {}", companyId);
  }

  @Override
  public PageImpl<CompanyWithUsersDto> getCompanies(int pageNumber, int pageSize) {
    List<CompanyWithUsersDto> resultDto = new LinkedList<>();
    Page<Company> companies =
        companyRepositoryHibernate.findAll(PageRequest.of(pageNumber, pageSize));

    for (Company company : companies) {
      if (Objects.nonNull(company.getEmployeeIds()) && !company.getEmployeeIds().isEmpty()) {
        String ids =
            company.getEmployeeIds().stream().map(String::valueOf).collect(Collectors.joining(","));
        List<UserDto> users = userServiceClient.getUsers(ids).getBody();
        resultDto.add(mapToUserWithCompanyDto(company, users));
      }
    }

    PageImpl<CompanyWithUsersDto> pageResult =
        new PageImpl(resultDto, PageRequest.of(pageNumber, pageSize), companies.getTotalElements());
    log.info(
        "Успешно получен список компаний с параметрами pageNumber = {}, pageSize = {}",
        pageNumber,
        pageSize);
    return pageResult;
  }
}
