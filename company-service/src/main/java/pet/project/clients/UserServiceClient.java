package pet.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pet.project.dto.UserWithCompanyDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

  @GetMapping("/user/{id}")
  ResponseEntity<UserWithCompanyDto> getUser(
      @PathVariable(value = "id") Integer userId,
      @RequestParam(value = "withCompanyInfo", defaultValue = "false") boolean withCompanyInfo);
}
