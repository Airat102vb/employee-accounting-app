package pet.project.mapper;

import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;

public class UserMapper {

  public static UserDto mapToUserDto(UserWithCompanyDto dto, int companyId) {
    return new UserDto(dto.id(), dto.firstName(), dto.lastName(), dto.phoneNumber(), companyId);
  }
}
