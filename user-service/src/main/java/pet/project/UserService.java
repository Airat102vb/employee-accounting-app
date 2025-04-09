package pet.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pet.project.dao.UserRepository;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import java.util.List;

@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long addUser(UserDto newUser) {
    return userRepository.addUser(newUser);
  }

  public UserWithCompanyDto getUserById(String userId) {
    return userRepository.getUserById(userId);
  }

  public UserDto update(UserDto newUserData) {
    return userRepository.update(newUserData);
  }

  public void deleteUser(String userId) {
    userRepository.deleteUser(userId);
  }

  public List<UserWithCompanyDto> getAllUsers() {
    return userRepository.getAllUsers();
  }
}
