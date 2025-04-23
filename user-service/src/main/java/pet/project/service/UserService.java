package pet.project.service;

import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import java.util.List;

public interface UserService {

  Integer addUser(UserDto newUser);

  UserWithCompanyDto getUser(Integer userId, boolean withCompanyInfo);

  UserDto update(Integer userId, UserDto newUserData);

  void deleteUser(Integer userId);

  List<UserWithCompanyDto> getAllUsers();
}
