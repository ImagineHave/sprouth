package xyz.imaginehave.sprouth.security.filters;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.RefreshToken;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.properties.SprouthSecurityProperties;
import xyz.imaginehave.sprouth.repository.RefreshTokenRepository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

public class SprouthJWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
	private AuthenticationManager authenticationManager;
	private SprouthSecurityProperties securityProperties;
	private RefreshTokenRepository refreshTokenRepository;
	private SprouthUserRepository sprouthUserRepository;


	public SprouthJWTAuthenticationFilter(AuthenticationManager authenticationManager,
			SprouthSecurityProperties securityProperties, RefreshTokenRepository refreshTokenRepository,
			SprouthUserRepository sprouthUserRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.securityProperties = securityProperties;
		this.refreshTokenRepository = refreshTokenRepository;
		this.sprouthUserRepository = sprouthUserRepository;
	}

	@Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            SprouthUser creds = new ObjectMapper()
                    .readValue(req.getInputStream(), SprouthUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	
    	Optional<SprouthUser> sprouthUser = sprouthUserRepository.findByUsername(auth.getName());
    	
    	Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUser(sprouthUser.get());
    	RefreshToken refreshToken = null;
    	
    	if(refreshTokenOptional.isPresent()) {
    		if(!refreshTokenOptional.get().isExpired()) {
    			refreshToken = refreshTokenOptional.get();
    		} else {
    			refreshTokenRepository.delete(refreshTokenOptional.get());
    		}
    	} 
    	
    	if(refreshToken == null){ 
    		refreshToken = new RefreshToken(sprouthUser.get());
    		refreshToken.setToken(createJWT(sprouthUser, refreshToken));
    		refreshTokenRepository.save(refreshToken);
    	}
    	
    	res.addHeader(securityProperties.getHeaderString(), securityProperties.getTokenPrefix() + refreshToken.getToken());
		
    }
    
	private String createJWT(Optional<SprouthUser> sprouthUser, RefreshToken refreshToken) {
		return JWT.create()
		.withSubject(sprouthUser.get().getUsername())
		.withExpiresAt(Date.from(refreshToken.getExpiryDate().atZone(ZoneId.systemDefault()).toInstant()))
		.sign(HMAC512(securityProperties.getPrivateKey().getBytes()));
	}
    
}
