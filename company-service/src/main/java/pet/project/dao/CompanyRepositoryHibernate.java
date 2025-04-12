package pet.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.project.entity.Company;

public interface CompanyRepositoryHibernate extends JpaRepository<Company, Integer> {}
