package xyz.imaginehave.sprouth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.imaginehave.sprouth.entity.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}
