package pet.project.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfigurationProperties {

  private final Logger logger = LoggerFactory.getLogger("DataSourceConfigurationProperties");
  private String url;
  private String username;
  private String password;

  @PostConstruct
  public void init() {
    logger.info("Loaded DataSource properties: url={}, username={}", url, username);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
