package pet.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "companies")
public class Company {

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
}
