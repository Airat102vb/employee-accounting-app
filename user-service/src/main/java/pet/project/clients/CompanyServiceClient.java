package pet.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.dto.CompanyDto;

@FeignClient(name = "company-service")
public interface CompanyServiceClient {

  @GetMapping("/get-by-user")
  ResponseEntity<CompanyDto> getCompanyByUser(@RequestParam("employeeId") String employeeId);
}
