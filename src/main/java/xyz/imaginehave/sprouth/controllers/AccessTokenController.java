package xyz.imaginehave.sprouth.controllers;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.AccessToken;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.properties.SprouthSecurityProperties;
import xyz.imaginehave.sprouth.repository.AccessTokenRepository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

@RestController
@RequestMapping("/access")
public class AccessTokenController {
	
	@Autowired
	private SprouthSecurityProperties securityProperties;
	
	@Autowired
	private AccessTokenRepository accessTokenRepository;
	
	@Autowired
	private SprouthUserRepository sprouthUserRepository;
	
    @GetMapping
    public AccessToken getAccessToken() {
    	
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	Optional<SprouthUser> sprouthUser = sprouthUserRepository.findByUsername(username);
    	
    	Optional<AccessToken> accessTokenOptional = accessTokenRepository.findByUser(sprouthUser.get());
    	if(accessTokenOptional.isPresent()) {
    		if(!accessTokenOptional.get().isExpired()) {
    			return accessTokenOptional.get();
    		} else {
    			accessTokenRepository.delete(accessTokenOptional.get());
    		}
    	}
		
    	AccessToken accessToken = new AccessToken(sprouthUser.get());
		accessToken.setToken(createJWT(sprouthUser, accessToken));
    	
        return accessTokenRepository.save(accessToken);
    }

	private String createJWT(Optional<SprouthUser> sprouthUser, AccessToken accessToken) {
		return JWT.create()
		.withSubject(sprouthUser.get().getUsername())
		.withExpiresAt(Date.from(accessToken.getExpiryDate().atZone(ZoneId.systemDefault()).toInstant()))
		.sign(HMAC512(securityProperties.getSharedKey().getBytes()));
	}

}
