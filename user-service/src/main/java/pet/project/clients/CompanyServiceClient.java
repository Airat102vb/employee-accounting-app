package pet.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyWithUsersDto;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

  @GetMapping("/company/{id}")
  ResponseEntity<CompanyWithUsersDto> getCompany(
      @PathVariable(value = "id") Integer companyId,
      @RequestParam(value = "withUserInfo", defaultValue = "false") boolean withUserInfo);
}
