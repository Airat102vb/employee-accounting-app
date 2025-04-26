package pet.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.project.dto.UserDto;
import pet.project.dto.UserWithCompanyDto;
import pet.project.service.UserService;

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
    Integer result = userService.addUser(newUser);
    return result == 0
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось добавить пользователя")
        : ResponseEntity.status(HttpStatus.CREATED)
            .body("Создан пользователь с id = %s".formatted(result));
  }

  @GetMapping("{id}")
  public ResponseEntity<UserWithCompanyDto> getUser(
      @PathVariable(value = "id") @Min(0) Integer userId,
      @RequestParam(value = "withCompanyInfo", defaultValue = "false") boolean withCompanyInfo) {
    UserWithCompanyDto user = userService.getUser(userId, withCompanyInfo);
    return ResponseEntity.ok(user);
  }

  @PutMapping("{id}")
  public ResponseEntity updateUser(
      @PathVariable(value = "id") Integer userId, @RequestBody UserDto newUserData) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, newUserData));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteUser(@PathVariable(value = "id") @Min(0) Integer userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping
  public ResponseEntity getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> onNoSuchElementException(NoSuchElementException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
  }
}
