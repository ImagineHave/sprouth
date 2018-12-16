package xyz.imaginehave.sprouth.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Optional;

import xyz.imaginehave.sprouth.entity.RefreshToken;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.properties.SprouthSecurityProperties;
import xyz.imaginehave.sprouth.repository.RefreshTokenRepository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

public class SprouthJWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private SprouthSecurityProperties securityProperties;
	private RefreshTokenRepository refreshTokenRepository;
	private SprouthUserRepository sprouthUserRepository;
	
    public SprouthJWTAuthorizationFilter(AuthenticationManager authenticationManager,
			SprouthSecurityProperties securityProperties,
			RefreshTokenRepository refreshTokenRepository, 
			SprouthUserRepository sprouthUserRepository) {
    	
		super(authenticationManager);
		this.securityProperties = securityProperties;
		this.refreshTokenRepository = refreshTokenRepository;
		this.sprouthUserRepository = sprouthUserRepository;
	}

	@Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(securityProperties.getHeaderString());
        if (header == null || !header.startsWith(securityProperties.getTokenPrefix())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    	
        String token = request.getHeader(securityProperties.getHeaderString());
        if (token != null) {
            
        	String user = getUserName(token);
            
        	Optional<SprouthUser> sprouthUser = sprouthUserRepository.findByUsername(user);
        	
        	Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUser(sprouthUser.get());
        	
        	if(refreshTokenOptional.isPresent()) {
        		if(!refreshTokenOptional.get().isExpired()) {
                    return new UsernamePasswordAuthenticationToken(user, null, sprouthUser.get().getAuthorities());
        		} else {
        			refreshTokenRepository.delete(refreshTokenOptional.get());
        			return null;
        		}
        	} 
            return null;
        }
        return null;
    }

	private String getUserName(String token) {
		return JWT.require(Algorithm.HMAC512(securityProperties.getPrivateKey().getBytes()))
		        .build()
		        .verify(token.replace(securityProperties.getTokenPrefix(), ""))
		        .getSubject();
	}

}