package pet.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

  @GetMapping("/company")
  public String  testCompany() {
    return  "Random company";
  }
}
