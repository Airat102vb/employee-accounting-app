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
import pet.project.dto.CompanyDto;
import pet.project.dto.CompanyInfoDto;

@Repository
public class CompanyRepository {

  private final Logger logger = LoggerFactory.getLogger("CompanyRepository");
  private DataSource dataSource;

  @Autowired
  public CompanyRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public CompanyInfoDto getCompanyById(String companyId) {
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM companies WHERE id = %s".formatted(companyId);
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      if (resultSet.next()) {
        return new CompanyInfoDto(
            resultSet.getInt("id"),
            resultSet.getString("company_name"),
            resultSet.getBigDecimal("budget"));
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public CompanyDto getCompanyByOfUserById(String userId) {
    Integer companyId = getCompanyOfEmployee(userId);
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM companies WHERE id = %s".formatted(companyId);
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      if (resultSet.next()) {
        return new CompanyDto(
            resultSet.getInt("id"),
            resultSet.getString("company_name"),
            resultSet.getBigDecimal("budget"),
            List.of(Integer.parseInt(userId)));
      }
      return new CompanyDto(null, null, null, null);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Integer> getEmployeeIdsOfCompany(String companyId) {
    List<Integer> ids = new LinkedList<>();
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM company_employees WHERE company_id = %s".formatted(companyId);
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      while (resultSet.next()) {
        ids.add(resultSet.getInt("employee_id"));
      }
      return ids;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Integer getCompanyOfEmployee(String employeeId) {
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM company_employees WHERE employee_id = %s".formatted(employeeId);
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      if (resultSet.next()) {
        return resultSet.getInt("company_id");
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Integer insertCompany(CompanyDto company) {
    String sql = "INSERT INTO companies (company_name, budget) VALUES (?, ?)";
    try (PreparedStatement statement =
        dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, company.companyName());
      statement.setBigDecimal(2, company.budget());

      logger.info("Выполняется запрос: \n{}", sql);
      statement.executeUpdate();
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Не удалось добавить компанию", e);
    }
    return null;
  }

  public int insertEmployeeToCompany(int companyId, int employeeId) {
    String sql = "INSERT INTO company_employees (company_id, employee_id) VALUES (?, ?)";
    try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
      statement.setInt(1, companyId);
      statement.setInt(2, employeeId);
      logger.info("Выполняется запрос: \n{}", sql);
      return statement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException("Не удалось добавить пользователя в компанию", e);
    }
  }

  public void updateCompany(CompanyDto newCompanyData) {
    String sql = "UPDATE companies SET company_name = ?, budget = ? WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      logger.info(
          "Executing UPDATE: {}, params: [{}, {}, {}]",
          sql,
          newCompanyData.companyName(),
          newCompanyData.budget(),
          newCompanyData.id());

      statement.setString(1, newCompanyData.companyName());
      statement.setBigDecimal(2, newCompanyData.budget());
      statement.setInt(3, newCompanyData.id());

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new RuntimeException("Company with id " + newCompanyData.id() + " not found");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update company", e);
    }
  }

  public boolean updateEmployees(CompanyDto newCompanyData) {
    newCompanyData
        .employeeId()
        .forEach(employeeId -> insertEmployeeToCompany(newCompanyData.id(), employeeId));
    return true;
  }

  public boolean deleteEmployees(Integer companyId) {
    String sqlDelete = "DELETE FROM company_employees WHERE company_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlDelete)) {

      statement.setInt(1, companyId);
      logger.info("Выполняется запрос: {} с параметром companyId = {}", sqlDelete, companyId);
      int affectedRows = statement.executeUpdate();

      return affectedRows != 0;

    } catch (SQLException e) {
      logger.error(
          "Ошибка при удалении сотрудников из компании с id {}: {}", companyId, e.getMessage());
      throw new RuntimeException("Не удалось удалить сотрудников", e);
    }
  }

  public boolean deleteCompanyById(String companyId) {
    String sql = "DELETE FROM companies WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, Integer.parseInt(companyId));
      logger.info("Выполняется запрос: {} с параметром companyId = {}", sql, companyId);

      int affectedRows = statement.executeUpdate();
      return affectedRows != 0;
    } catch (SQLException e) {
      logger.error("Ошибка при удалении компании с id {}: {}", companyId, e.getMessage());
      throw new RuntimeException("Не удалось удалить компанию", e);
    }
  }

  public List<CompanyInfoDto> getAllCompanies() {
    List<CompanyInfoDto> companies = new LinkedList<>();
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM companies;";
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      while (resultSet.next()) {
        companies.add(
            new CompanyInfoDto(
                resultSet.getInt("id"),
                resultSet.getString("company_name"),
                resultSet.getBigDecimal("budget")));
      }
      return companies;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
