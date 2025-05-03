package pet.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.entity.User;
import pet.project.service.UserService;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity addUser(@Valid @RequestBody UserDto newUser) {
    log.info("Добавление сотрудника: {}", newUser);
    User user = userService.addUser(newUser);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();
    return ResponseEntity.created(uri).body(user);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserWithCompanyDto> getUser(
      @PathVariable(value = "id") @Min(1) Integer userId,
      @RequestParam(value = "withCompanyInfo", defaultValue = "false") boolean withCompanyInfo) {
    log.info("Получение сотрудника: {}, с флагом withCompanyInfo {}", userId, withCompanyInfo);
    UserWithCompanyDto user = userService.getUser(userId, withCompanyInfo);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/page")
  public ResponseEntity getUsers(
      @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    log.info(
        "Получение списка сотрудников со станицы {}, с количеством на странице {}",
        pageNumber,
        pageSize);
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(pageNumber, pageSize));
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(
      @RequestHeader(name = "X-Employee-IDs") String employeeIds) {
    log.info("Получение списка сотрудников по id: {}", employeeIds);
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(employeeIds));
  }

  @PutMapping("{id}")
  public ResponseEntity updateUser(
      @PathVariable(value = "id") Integer userId, @RequestBody UserDto newUserData) {
    log.info("Получение сотрудника: {}, новыми данными {}", userId, newUserData);
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, newUserData));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteUser(@PathVariable(value = "id") @Min(1) Integer userId) {
    log.info("Удаление сотрудника: {}", userId);
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
