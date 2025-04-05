package pet.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigChecker implements CommandLineRunner {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.username}")
  private String dbUser;

  private final Environment env;

  @Autowired
  public ConfigChecker(Environment env) {
    this.env = env;
  }

  @Override
  public void run(String... args) {
    System.out.println("=== Active Profiles ===");
    for (String profile : env.getActiveProfiles()) {
      System.out.println(profile);
    }

    System.out.println("=== Config from Config Server ===");
    System.out.println("DB URL: " + dbUrl);
    System.out.println("DB User: " + dbUser);
    System.out.println("================================");
  }
}