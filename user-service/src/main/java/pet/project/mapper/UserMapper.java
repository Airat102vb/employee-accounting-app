package pet.project.mapper;

import pet.project.dto.CompanyDto;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.entity.User;

public class UserMapper {

  public static UserDto mapToUserDto(User user) {
    return new UserDto(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getPhoneNumber(),
        user.getCompanyId());
  }

  public static User mapToUser(UserDto userDto) {
    return new User(
        userDto.id(),
        userDto.firstName(),
        userDto.lastName(),
        userDto.phoneNumber(),
        userDto.companyId());
  }

  public static UserWithCompanyDto mapToUserWithCompanyDto(User user, String companyName) {
    return new UserWithCompanyDto(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getPhoneNumber(),
        companyName);
  }

  public static UserWithCompanyDto mapToUserWithCompanyDto(User user) {
    return new UserWithCompanyDto(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getPhoneNumber(),
        null);
  }
}
