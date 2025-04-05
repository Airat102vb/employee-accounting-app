package pet.project.config;

import javax.sql.DataSource;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@ComponentScan("pet.project")
public class Config {

  private DataSourceConfigurationProperties dataSourceProperties;

  @Autowired
  public Config(DataSourceConfigurationProperties dataSourceConfigurationProperties) {
    this.dataSourceProperties = dataSourceConfigurationProperties;
  }

  @Bean
  public DataSource dataSource() {
    return new SimpleDriverDataSource(
        new Driver(),
        dataSourceProperties.getUrl(),
        dataSourceProperties.getUsername(),
        dataSourceProperties.getPassword());
  }
}
