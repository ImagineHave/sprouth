package xyz.imaginehave.sprouth.cli;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.entity.SprouthGrantedAuthority;
import xyz.imaginehave.sprouth.repository.SprouthGrantedAuthorityRespository;

@Slf4j
@Configuration
public class LoadSprouthGrantedAuthority {
	
	private final String USER = "USER";
	private final String ADMIN = "ADMIN";
	
	@Bean
	CommandLineRunner initDatabase(SprouthGrantedAuthorityRespository sprouthGrantedAuthorityRespository) {
		return args -> {
			
			log.info("Count: " + sprouthGrantedAuthorityRespository.count());
			Optional<SprouthGrantedAuthority> optionalUser = sprouthGrantedAuthorityRespository.findByAuthority(USER);
			
			Optional<SprouthGrantedAuthority> optionalAdmin = sprouthGrantedAuthorityRespository.findByAuthority(ADMIN);
			

			if(!optionalUser.isPresent()) {
				SprouthGrantedAuthority user = new SprouthGrantedAuthority(USER);
				log.info("Preloading USER authority: " + sprouthGrantedAuthorityRespository.save(user));
			} else {
				log.info("User: " + optionalUser.get());
			}
			
			if(!optionalAdmin.isPresent()) {
				SprouthGrantedAuthority admin = new SprouthGrantedAuthority(ADMIN);
				log.info("Preloading ADMIN authority: " + sprouthGrantedAuthorityRespository.save(admin));
			} else {
				log.info("Admin: " + optionalAdmin.get());
			}

		};
	}

}
