package pet.project.service;

import org.springframework.data.domain.PageImpl;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.entity.Company;

public interface CompanyService {

  Company addCompany(CompanyDto newCompany);

  void insertEmployeeToCompany(Integer companyId, Integer employeeId);

  CompanyWithUsersDto getCompany(Integer companyId, boolean withUserInfo);

  void updateCompany(Integer companyId, CompanyDto newCompanyData);

  void deleteCompany(Integer companyId);

  PageImpl<CompanyWithUsersDto> getCompanies(int pageNumber, int pageSize);
}
