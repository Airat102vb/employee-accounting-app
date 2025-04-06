package pet.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pet.project.dto.UserDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

  @GetMapping("/get")
  ResponseEntity<UserDto> getUser(@RequestParam("userId") String userId);
}
