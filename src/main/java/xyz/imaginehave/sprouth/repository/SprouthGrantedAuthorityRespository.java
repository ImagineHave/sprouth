package xyz.imaginehave.sprouth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import xyz.imaginehave.sprouth.entity.SprouthGrantedAuthority;

@Repository
@Transactional
public interface SprouthGrantedAuthorityRespository extends JpaRepository<SprouthGrantedAuthority, Long> {
	
	Optional<SprouthGrantedAuthority> findByAuthority(String authority);
}
