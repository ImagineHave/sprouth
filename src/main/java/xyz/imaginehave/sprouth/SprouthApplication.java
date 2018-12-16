package xyz.imaginehave.sprouth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration
@EnableJpaAuditing
@EnableEmailTools
public class SprouthApplication {
	
	public static void main(String[] args) {
		log.info("DB_ADDRESS: " + System.getenv("DB_ADDRESS"));
		log.info("DB_NAME: " + System.getenv("DB_NAME"));
		SpringApplication.run(SprouthApplication.class, args);
	}
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
