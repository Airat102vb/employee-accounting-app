package pet.project.service;

import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.entity.Company;
import java.util.List;

public interface CompanyService {

  Company addCompany(CompanyDto newCompany);

  void insertEmployeeToCompany(Integer companyId, Integer employeeId);

  CompanyWithUsersDto getCompany(Integer companyId, boolean withUserInfo);

  void updateCompany(Integer companyId, CompanyDto newCompanyData);

  void deleteCompany(Integer companyId);

  List<CompanyWithUsersDto> getAllCompanies();
}
