package pet.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

  public Company() {}

  public Company(Integer id, String companyName, BigDecimal budget, List<Integer> employeeIds) {
    this.id = id;
    this.companyName = companyName;
    this.budget = budget;
    this.employeeIds = employeeIds;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @Column(name = "company_name")
  String companyName;

  @Column(name = "budget")
  BigDecimal budget;

  @Column(name = "employee_ids")
  List<Integer> employeeIds;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public List<Integer> getEmployeeIds() {
    return employeeIds;
  }

  public void setEmployeeIds(List<Integer> employeeIds) {
    this.employeeIds = employeeIds;
  }
}
