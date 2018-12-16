package xyz.imaginehave.sprouth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import xyz.imaginehave.sprouth.security.filters.SprouthJWTAuthenticationFilter;
import xyz.imaginehave.sprouth.security.filters.SprouthJWTAuthorizationFilter;
import xyz.imaginehave.sprouth.security.properties.SprouthSecurityProperties;
import xyz.imaginehave.sprouth.service.SprouthUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private SprouthUserDetailsService userDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@Autowired
	private SprouthSecurityProperties securityProperties;
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public WebSecurity(SprouthUserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sprouth/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getJWTAuthenticationFilter())
                .addFilter(new SprouthJWTAuthorizationFilter(authenticationManager(), securityProperties))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    
    public SprouthJWTAuthenticationFilter getJWTAuthenticationFilter() throws Exception {
        final SprouthJWTAuthenticationFilter filter = new SprouthJWTAuthenticationFilter(authenticationManager(), securityProperties);
        filter.setFilterProcessesUrl("/sprouth/login");
        return filter;
    }
    
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    	return source;
    }
}