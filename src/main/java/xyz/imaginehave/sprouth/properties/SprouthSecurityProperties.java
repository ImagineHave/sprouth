package xyz.imaginehave.sprouth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "xyz.imagine-have.sprouth.security")
@Configuration()
@Data
public class SprouthSecurityProperties {
    
	private String sharedKey;
    private String expirationTime; 
    private String tokenPrefix;
    private String headerString;
    private String privateKey;
    
    public Long getExpirationTime() {
    	return Long.parseLong(expirationTime);
    }
}
