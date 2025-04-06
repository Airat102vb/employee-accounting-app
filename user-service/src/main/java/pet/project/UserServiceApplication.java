package pet.project;

import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import pet.project.config.Config;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Import(Config.class)
public class UserServiceApplication {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext();
    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

    SpringApplication.run(UserServiceApplication.class, args);
  }
}
