package pet.project.dto;

import java.math.BigDecimal;
import java.util.List;

public record CompanyWithUsersDto(
    Integer id,
    String companyName,
    BigDecimal budget,
    List<UserDto> employees
) {}
