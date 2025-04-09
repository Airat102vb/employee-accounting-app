package pet.project.controller;

import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pet.project.UserService;
import pet.project.dto.UserDto;
import pet.project.dao.UserRepository;
import pet.project.dto.UserWithCompanyDto;

@RestController
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/add")
  public ResponseEntity addUser(@RequestBody UserDto newUser) {
    Long result = userService.addUser(newUser);
    return result == 0
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось добавить пользователя")
        : ResponseEntity.status(HttpStatus.CREATED)
            .body("Создан пользователь с id = %s".formatted(result));
  }

  @GetMapping("/get")
  public ResponseEntity getUser(@QueryParam(value = "userId") String userId) {
    UserWithCompanyDto user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PutMapping("/update")
  public ResponseEntity updateUser(@RequestBody UserDto newUserData) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(newUserData));
  }

  @DeleteMapping("/delete")
  public ResponseEntity deleteUser(@QueryParam(value = "userId") String userId) {
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/all")
  public ResponseEntity getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }
}
