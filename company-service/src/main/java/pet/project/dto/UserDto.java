package pet.project.dto;

public record UserDto(
    Integer id,
    String firstName,
    String lastName,
    String phoneNumber,
    Integer companyId
) {}
