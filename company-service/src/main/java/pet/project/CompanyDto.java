package pet.project;

import java.math.BigDecimal;
import java.util.List;

public record CompanyDto(
    Integer id,
    String companyName,
    BigDecimal budget,
    List<Integer> employeeId
) {}
