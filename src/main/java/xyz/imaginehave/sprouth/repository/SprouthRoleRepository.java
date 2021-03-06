package xyz.imaginehave.sprouth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.SprouthRole;

@Repository
@Transactional
public interface SprouthRoleRepository extends JpaRepository<SprouthRole, Long> {
	
	Optional<SprouthRole> findByName(String name);

}