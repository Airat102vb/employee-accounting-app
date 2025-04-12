package pet.project;

import static pet.project.mapper.UserMapper.mapToUser;
import static pet.project.mapper.UserMapper.mapToUserDto;
import static pet.project.mapper.UserMapper.mapToUserWithCompanyDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.project.clients.CompanyServiceClient;
import pet.project.dao.UserRepositoryHibernate;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.entity.User;

@Service
public class UserService {

  private final Logger logger = LoggerFactory.getLogger("UserService");
  private CompanyServiceClient companyServiceClient;
  private UserRepositoryHibernate userRepositoryHibernate;

  @Autowired
  public UserService(
      UserRepositoryHibernate userRepositoryHibernate, CompanyServiceClient companyServiceClient) {
    this.companyServiceClient = companyServiceClient;
    this.userRepositoryHibernate = userRepositoryHibernate;
  }

  public Integer addUser(UserDto newUser) {
    User newCratedUser = userRepositoryHibernate.save(mapToUser(newUser));
    return mapToUserDto(newCratedUser).id();
  }

  public UserWithCompanyDto getUser(Integer userId, boolean withCompanyInfo) {
    User user = userRepositoryHibernate.findById(userId).orElseThrow();
    if (withCompanyInfo && Objects.nonNull(user.getCompanyId())) {
      CompanyWithUsersDto company =
          companyServiceClient.getCompany(user.getCompanyId(), false).getBody();
      return mapToUserWithCompanyDto(user, company.companyName());
    }
    return mapToUserWithCompanyDto(user);
  }

  public UserDto update(Integer userId, UserDto newUserData) {
    User user = userRepositoryHibernate.findById(userId).orElseThrow();

    user.setFirstName(newUserData.firstName());
    user.setLastName(newUserData.lastName());
    user.setPhoneNumber(newUserData.phoneNumber());
    user.setCompanyId(newUserData.companyId());

    User updatedUserDto = userRepositoryHibernate.save(user);
    return mapToUserDto(updatedUserDto);
  }

  public void deleteUser(Integer userId) {
    userRepositoryHibernate.deleteById(userId);
  }

  public List<UserWithCompanyDto> getAllUsers() {
    List<UserWithCompanyDto> resultDto = new LinkedList<>();
    List<User> users = userRepositoryHibernate.findAll();

    for (User user : users) {
      CompanyWithUsersDto companyDto =
          companyServiceClient.getCompany(user.getCompanyId(), false).getBody();
      resultDto.add(
          new UserWithCompanyDto(
              user.getId(),
              user.getFirstName(),
              user.getLastName(),
              user.getPhoneNumber(),
              companyDto.companyName()));
    }
    return resultDto;
  }
}
