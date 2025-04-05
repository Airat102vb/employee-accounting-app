package pet.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class CompanyServiceApplication {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext();
    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

    SpringApplication.run(CompanyServiceApplication.class, args);
  }
}
