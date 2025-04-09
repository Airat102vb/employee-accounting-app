package pet.project;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.project.clients.CompanyServiceClient;
import pet.project.dao.UserRepository;
import pet.project.dto.CompanyDto;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;

@Service
public class UserService {

  private final Logger logger = LoggerFactory.getLogger("UserService");
  private UserRepository userRepository;
  private CompanyServiceClient companyServiceClient;

  @Autowired
  public UserService(UserRepository userRepository, CompanyServiceClient companyServiceClient) {
    this.userRepository = userRepository;
    this.companyServiceClient = companyServiceClient;
  }

  public Long addUser(UserDto newUser) {
    return userRepository.addUser(newUser);
  }

  public UserWithCompanyDto getUser(String userId) {
    UserDto user = userRepository.getUserById(userId);
    if (Objects.nonNull(user.companyId())) {
      CompanyDto company = companyServiceClient.getCompanyByUser(userId).getBody();
      return new UserWithCompanyDto(
          user.id(), user.firstName(), user.lastName(), user.phoneNumber(), company.companyName());
    }
    return new UserWithCompanyDto(
        user.id(), user.firstName(), user.lastName(), user.phoneNumber(), null);
  }

  public UserDto updateUser(UserDto newUserData) {
    return userRepository.updateUserById(newUserData);
  }

  public void deleteUser(String userId) {
    if (userRepository.deleteUserById(userId)) {
      logger.info("Пользователь с id {} успешно удален", userId);
    } else {
      logger.warn("Пользователь с id {} не найден", userId);
    }
  }

  public List<UserWithCompanyDto> getAllUsers() {
    List<UserWithCompanyDto> usersWithCompany = new LinkedList<>();
    List<UserDto> users = userRepository.getAllUsers();

    users.forEach(
        user -> {
          CompanyDto company =
              companyServiceClient.getCompanyByUser(user.companyId().toString()).getBody();
          usersWithCompany.add(
              new UserWithCompanyDto(
                  user.id(),
                  user.firstName(),
                  user.lastName(),
                  user.phoneNumber(),
                  company.companyName()));
        });

    return usersWithCompany;
  }
}
