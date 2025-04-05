package pet.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import pet.project.config.Config;
import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
@Import(Config.class)
public class CompanyServiceApplication {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext();
    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

    SpringApplication.run(CompanyServiceApplication.class, args);
  }
}
