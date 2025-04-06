package pet.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pet.project.dto.UserDto;

@Repository
public class UserRepository {

  private final Logger logger = LoggerFactory.getLogger("UserRepository");
  private DataSource dataSource;

  @Autowired
  public UserRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public UserDto getUserById(String userId) {
    try (Connection connection = dataSource.getConnection()) {
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
            null);
      }
      return new UserDto(null, null, null, null, null);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Long addUser(UserDto user) {
    String sql =
        "INSERT INTO users (first_name, last_name, phone_number, company_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement =
        dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.firstName());
      statement.setString(2, user.lastName());
      statement.setString(3, user.phoneNumber());
      statement.setInt(4, user.companyId());
      logger.info("Выполняется запрос: \n{}", sql);
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

  public UserDto update(UserDto newUserData) {
    String sql =
        "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, company_id = ? WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      logger.info(
          "Executing UPDATE: {}, params: [{}, {}, {}, {}, {}]",
          sql,
          newUserData.firstName(),
          newUserData.lastName(),
          newUserData.phoneNumber(),
          newUserData.companyId(),
          newUserData.id());

      statement.setString(1, newUserData.firstName());
      statement.setString(2, newUserData.lastName());
      statement.setString(3, newUserData.phoneNumber());
      statement.setInt(4, newUserData.companyId());
      statement.setInt(5, newUserData.id());

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new RuntimeException("User with id " + newUserData.id() + " not found");
      }

      return newUserData;

    } catch (SQLException e) {
      logger.error("Failed to update user: {}", e.getMessage());
      throw new RuntimeException("Failed to update user", e);
    }
  }

  public boolean deleteUser(String userId) {
    String sql = "DELETE FROM users WHERE id = ?";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, Integer.parseInt(userId));
      logger.info("Выполняется запрос: {} с параметром userId = {}", sql, userId);

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warn("Пользователь с id {} не найден", userId);
        return false;
      }

      logger.info("Пользователь с id {} успешно удален", userId);
      return true;

    } catch (SQLException e) {
      logger.error("Ошибка при удалении пользователя с id {}: {}", userId, e.getMessage());
      throw new RuntimeException("Не удалось удалить пользователя", e);
    }
  }

  public List<UserDto> getAllUsers() {
    List<UserDto> users = new LinkedList<>();
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM users;";
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      while (resultSet.next()) {
        users.add(
            new UserDto(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                null,
                null));
      }
      return users;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
