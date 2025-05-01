package pet.project.service;

import static pet.project.mapper.UserMapper.mapToUser;
import static pet.project.mapper.UserMapper.mapToUserDto;
import static pet.project.mapper.UserMapper.mapToUserWithCompanyDto;

import jakarta.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pet.project.clients.CompanyServiceClient;
import pet.project.dao.UserRepositoryHibernate;
import pet.project.dto.CompanyWithUsersDto;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.entity.User;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private CompanyServiceClient companyServiceClient;
  private UserRepositoryHibernate userRepositoryHibernate;

  @Autowired
  public UserServiceImpl(
      UserRepositoryHibernate userRepositoryHibernate, CompanyServiceClient companyServiceClient) {
    this.companyServiceClient = companyServiceClient;
    this.userRepositoryHibernate = userRepositoryHibernate;
  }

  @Override
  public User addUser(@Valid UserDto newUser) {
    User user = userRepositoryHibernate.save(mapToUser(newUser));
    log.info("Новый сотрудник успешно добавлен: {}", user);
    return user;
  }

  @Override
  public UserWithCompanyDto getUser(Integer userId, boolean withCompanyInfo) {
    User user = userRepositoryHibernate.findById(userId).orElseThrow();
    if (withCompanyInfo && Objects.nonNull(user.getCompanyId())) {
      CompanyWithUsersDto company =
          companyServiceClient.getCompany(user.getCompanyId(), false).getBody();
      return mapToUserWithCompanyDto(user, company.companyName());
    }

    UserWithCompanyDto userWithCompanyDto = mapToUserWithCompanyDto(user);
    log.info("Сотрудник успешно получен: {}", userWithCompanyDto);
    return userWithCompanyDto;
  }

  @Override
  public UserDto update(Integer userId, @Valid UserDto newUserData) {
    User user = userRepositoryHibernate.findById(userId).orElseThrow();
    User userData = mapToUser(newUserData);
    userData.setId(user.getId());
    User updatedUserDto = userRepositoryHibernate.save(userData);

    UserDto userDto = mapToUserDto(updatedUserDto);
    log.info("Сотрудник успешно обновлен: {}", userDto);
    return userDto;
  }

  @Override
  public void deleteUser(Integer userId) {
    userRepositoryHibernate.deleteById(userId);
    log.info("Сотрудник удален: {}", userId);
  }

  @Override
  public PageImpl<UserWithCompanyDto> getUsers(int pageNumber, int pageSize) {
    List<UserWithCompanyDto> resultDto = new LinkedList<>();
    Page<User> users = userRepositoryHibernate.findAll(PageRequest.of(pageNumber, pageSize));

    for (User user : users) {
      CompanyWithUsersDto companyDto =
          companyServiceClient.getCompany(user.getCompanyId(), false).getBody();
      resultDto.add(mapToUserWithCompanyDto(user, companyDto.companyName()));
    }

    PageImpl<UserWithCompanyDto> page =
        new PageImpl<>(resultDto, PageRequest.of(pageNumber, pageSize), users.getTotalElements());
    log.info(
        "Успешно получен список сотрудников с параметрами pageNumber = {}, pageSize = {}",
        pageNumber,
        pageSize);
    return page;
  }
}
