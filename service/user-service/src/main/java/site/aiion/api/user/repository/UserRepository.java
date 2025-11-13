package site.aiion.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.aiion.api.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
