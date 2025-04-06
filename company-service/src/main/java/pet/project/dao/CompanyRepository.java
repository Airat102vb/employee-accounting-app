package pet.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pet.project.CompanyDto;

@Repository
public class CompanyRepository {

  private final Logger logger = LoggerFactory.getLogger("UserRepository");
  private DataSource dataSource;

  @Autowired
  public CompanyRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public CompanyDto getCompanyById(String companyId) {
    List<Integer> employeeIds = getEmployeesOfCompany(companyId);
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
            employeeIds);
      }
      return new CompanyDto(null, null, null, null);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Integer> getEmployeesOfCompany(String companyId) {
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

  public int insertCompany(CompanyDto company) {
    String sql = "INSERT INTO companies (company_name, budget) VALUES (?, ?)";
    try (PreparedStatement statement =
        dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, company.companyName());
      statement.setBigDecimal(2, company.budget());

      logger.info("Выполняется запрос: \n{}", sql);
      statement.executeUpdate();
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int companyId = generatedKeys.getInt(1);
          if (Objects.nonNull(company.employeeId()) && company.employeeId().size() != 0) {
            company
                .employeeId()
                .stream()
                .forEach(employeeId -> insertEmployeeToCompany(companyId, employeeId));
          }
          return companyId;
        }
      }
    } catch (SQLException e) {
      System.err.println("SQL Error State: " + e.getSQLState());
      System.err.println("Error Code: " + e.getErrorCode());
      System.err.println("Message: " + e.getMessage());
      System.err.println("Stack Trace:");
      e.printStackTrace();
      throw new RuntimeException("Не удалось добавить компанию", e);
    }
    return 0;
  }

  public int insertEmployeeToCompany(int companyId, int employeeId) {
    String sql = "INSERT INTO company_employees (company_id, employee_id) VALUES (?, ?)";
    try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
      statement.setInt(1, companyId);
      statement.setInt(2, employeeId);
      logger.info("Выполняется запрос: \n{}", sql);
      return statement.executeUpdate();

    } catch (SQLException e) {
      System.err.println("SQL Error State: " + e.getSQLState());
      System.err.println("Error Code: " + e.getErrorCode());
      System.err.println("Message: " + e.getMessage());
      System.err.println("Stack Trace:");
      e.printStackTrace();
      throw new RuntimeException("Не удалось добавить пользователя в компанию", e);
    }
  }

  public void update(CompanyDto newCompanyData) {
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
      logger.error("Failed to update company: {}", e.getMessage());
      throw new RuntimeException("Failed to update company", e);
    }

    if (Objects.nonNull(newCompanyData.employeeId()) && newCompanyData.employeeId().size() != 0) {
      updateEmployees(newCompanyData);
    }
  }

  private boolean updateEmployees(CompanyDto newCompanyData) {
    deleteEmployees(newCompanyData.id());
    newCompanyData.employeeId().stream()
        .forEach(employeeId -> insertEmployeeToCompany(newCompanyData.id(), employeeId));
    return true;
  }

  private void deleteEmployees(Integer companyId) {
    String sqlDelete = "DELETE FROM company_employees WHERE company_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlDelete)) {

      statement.setInt(1, companyId);
      logger.info("Выполняется запрос: {} с параметром companyId = {}", sqlDelete, companyId);
      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        logger.warn("В компании с id {} не было сотрудников", companyId);
      }
      logger.info("Сотрудники из компании {} успешно удалены", companyId);
    } catch (SQLException e) {
      logger.error(
          "Ошибка при удалении сотрудников из компании с id {}: {}", companyId, e.getMessage());
      throw new RuntimeException("Не удалось удалить сотрудников", e);
    }
  }

  public boolean deleteCompany(String companyId) {
    deleteEmployees(Integer.parseInt(companyId));

    String sql = "DELETE FROM companies WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, Integer.parseInt(companyId));
      logger.info("Выполняется запрос: {} с параметром companyId = {}", sql, companyId);

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        logger.warn("Компания с id {} не найдена", companyId);
        return false;
      }
      logger.info("Компания с id {} успешно удалена", companyId);
      return true;
    } catch (SQLException e) {
      logger.error("Ошибка при удалении компании с id {}: {}", companyId, e.getMessage());
      throw new RuntimeException("Не удалось удалить компанию", e);
    }
  }

  public List<CompanyDto> getAllCompanies() {
    List<CompanyDto> companies = new LinkedList<>();
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM companies;";
      logger.info("Выполняется запрос: \n{}", sql);
      ResultSet resultSet = statement.executeQuery(sql);

      while (resultSet.next()) {
        int companyId = resultSet.getInt("id");
        List<Integer> employeeIds = getEmployeesOfCompany(String.valueOf(companyId));
        companies.add(
            new CompanyDto(
                companyId,
                resultSet.getString("company_name"),
                resultSet.getBigDecimal("budget"),
                employeeIds));
      }
      return companies;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
