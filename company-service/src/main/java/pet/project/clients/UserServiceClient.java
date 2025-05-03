package pet.project.clients;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import pet.project.dto.UserDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

  @GetMapping("/user")
  ResponseEntity<List<UserDto>> getUsers(
      @RequestHeader(name = "X-Employee-IDs") String employeeIds);
}
