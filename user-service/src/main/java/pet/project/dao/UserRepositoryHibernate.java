package pet.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.project.entity.User;

public interface UserRepositoryHibernate extends JpaRepository<User, Integer> {}
