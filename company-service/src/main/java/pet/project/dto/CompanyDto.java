package pet.project.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public record CompanyDto(
    Integer id,
    @NotBlank String companyName,
    @Digits(integer = 15, fraction = 2) BigDecimal budget,
    List<Integer> employeeId
) {}
