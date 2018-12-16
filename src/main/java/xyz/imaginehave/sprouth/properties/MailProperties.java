package xyz.imaginehave.sprouth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "xyz.imagine-have.sprouth.mail")
@Configuration()
@Data
public class MailProperties {
	
	private String host;
	private String port;
	private String username;
	private String password;
	private String email;
	private String name;
	private String verificationapi;

}
