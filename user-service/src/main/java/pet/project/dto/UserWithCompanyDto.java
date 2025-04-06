package pet.project.dto;

public record UserWithCompanyDto(
    Integer id,
    String firstName,
    String lastName,
    String phoneNumber,
    String companyName
) {}
