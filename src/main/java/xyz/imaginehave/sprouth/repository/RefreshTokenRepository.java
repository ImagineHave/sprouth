package xyz.imaginehave.sprouth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.RefreshToken;
import xyz.imaginehave.sprouth.entity.SprouthUser;

@Repository
@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	public Optional<RefreshToken> findByUser(SprouthUser user); 

}
