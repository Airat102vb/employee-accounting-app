package pet.project.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
    Integer id,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull @Digits(integer = 11, fraction = 0) String phoneNumber,
    Integer companyId) {}
