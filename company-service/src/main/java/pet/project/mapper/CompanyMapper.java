package pet.project.mapper;

import java.util.List;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;
import pet.project.entity.Company;

public class CompanyMapper {

  public static CompanyDto mapToCompanyDto(Company company) {
    return new CompanyDto(company.getId(), company.getCompanyName(), company.getBudget(), null);
  }

  public static List<CompanyDto> mapToCompanyDtoList(List<Company> company) {
    return company.stream().map(CompanyMapper::mapToCompanyDto).toList();
  }

  public static Company mapToCompany(CompanyDto companyDto) {
    return new Company(
        companyDto.id(), companyDto.companyName(), companyDto.budget(), companyDto.employeeId());
  }

  public static CompanyWithUsersDto mapToUserWithCompanyDto(Company company, List<UserDto> users) {
    return new CompanyWithUsersDto(
        company.getId(), company.getCompanyName(), company.getBudget(), users);
  }
}
