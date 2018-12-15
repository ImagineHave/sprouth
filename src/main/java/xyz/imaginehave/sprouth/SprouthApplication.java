package xyz.imaginehave.sprouth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableAutoConfiguration
public class SprouthApplication {
	
	public static void main(String[] args) {
		log.info("DB_ADDRESS: " + System.getenv("DB_ADDRESS"));
		log.info("DB_NAME: " + System.getenv("DB_NAME"));
		log.info("EXPIRATION_TIME: " + System.getenv("EXPIRATION_TIME"));
		SpringApplication.run(SprouthApplication.class, args);
	}
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
