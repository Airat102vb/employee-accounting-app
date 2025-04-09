package pet.project;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.project.dao.CompanyRepository;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;

@Service
public class CompanyService {

  private CompanyRepository companyRepository;

  @Autowired
  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public Integer insertCompany(CompanyDto newCompany) {
    return companyRepository.insertCompany(newCompany);
  }

  public Integer insertEmployeeToCompany(Integer companyId, Integer employeeId) {
    return companyRepository.insertEmployeeToCompany(companyId, employeeId);
  }

  public CompanyWithUsersDto getCompanyById(String companyId) {
    return companyRepository.getCompanyById(companyId);
  }

  public CompanyDto getCompanyByUserId(String employeeId) {
    return companyRepository.getCompanyByUserId(employeeId);
  }

  public void update(CompanyDto newCompanyData) {
    companyRepository.update(newCompanyData);
  }

  public boolean deleteCompany(String companyId) {
    return companyRepository.deleteCompany(companyId);
  }

  public List<CompanyWithUsersDto> getAllCompanies() {
    return companyRepository.getAllCompanies();
  }
}
