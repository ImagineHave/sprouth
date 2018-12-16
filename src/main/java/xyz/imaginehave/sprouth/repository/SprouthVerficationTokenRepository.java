package xyz.imaginehave.sprouth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;

@Repository
public interface SprouthVerficationTokenRepository extends JpaRepository<SprouthVerificationToken, Long> {

	Optional<SprouthVerificationToken> findByToken(String token);

	Optional<SprouthVerificationToken> findByUser(SprouthUser user);

}
