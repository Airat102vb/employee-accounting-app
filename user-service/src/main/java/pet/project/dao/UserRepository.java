package pet.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pet.project.config.dto.UserDto;

@Repository
public class UserRepository {

  private final Logger logger = LoggerFactory.getLogger("UserRepository");
  private DataSource dataSource;

  @Autowired
  public UserRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public UserDto getUserById(String userId) {
    try(Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM users WHERE id = %s".formatted(userId);
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      if (resultSet.next()) {
            return new UserDto(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                null,
                null
        );
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Long getAddUser(UserDto user) {
    String sql = "INSERT INTO users (first_name, last_name, phone_number, company_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.firstName());
      statement.setString(2, user.lastName());
      statement.setString(3, user.phoneNumber());
      statement.setInt(4, user.companyId());
      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getLong(1);
        }
      }
    } catch (SQLException e) {
      System.err.println("SQL Error State: " + e.getSQLState());
      System.err.println("Error Code: " + e.getErrorCode());
      System.err.println("Message: " + e.getMessage());
      System.err.println("Stack Trace:");
      e.printStackTrace();
      throw new RuntimeException("Не удалось добавить пользователя", e);
    }
    return 0L;
  }
}
