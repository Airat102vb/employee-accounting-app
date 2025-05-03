package pet.project.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.entity.User;
import java.util.List;

public interface UserService {

  User addUser(UserDto newUser);

  UserWithCompanyDto getUser(Integer userId, boolean withCompanyInfo);

  UserDto update(Integer userId, UserDto newUserData);

  void deleteUser(Integer userId);

  PageImpl<UserWithCompanyDto> getUsers(int pageNumber, int pageSize);

  List<UserDto> getUsers(String employeeIds);
}
