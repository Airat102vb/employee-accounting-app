package pet.project.controller;

import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pet.project.config.dto.UserDto;
import pet.project.dao.UserRepository;

@RestController
public class UserController {

  private UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/test")
  public String test() {
    return "test";
  }

  @PostMapping("/add")
  public ResponseEntity addUser(@RequestBody UserDto newUser) {
    Long result = userRepository.getAddUser(newUser);
    return result == 0 ?
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось добавить пользователя") :
        ResponseEntity.status(HttpStatus.CREATED).body("Создан пользователь с id = %s".formatted(result));
  }

  @GetMapping("/get")
  public ResponseEntity getUser(@QueryParam(value = "userId") String userId) {
    UserDto user = userRepository.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PutMapping("/update")
  public ResponseEntity updateUser(@RequestBody UserDto newUser) {
    return ResponseEntity.ok("");
  }

  @GetMapping("/delete")
  public ResponseEntity deleteUser(@RequestBody UserDto newUser) {
    return ResponseEntity.ok("");
  }
}
