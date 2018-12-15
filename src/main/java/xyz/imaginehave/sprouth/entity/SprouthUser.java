package xyz.imaginehave.sprouth.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Data
public class SprouthUser implements UserDetails {
    
	private static final long serialVersionUID = 27584725850867301L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
	@Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	
	public SprouthUser() {
		this.setAuthorities(new HashSet<>());
		this.setAccountNonExpired(true);
		this.setAccountNonLocked(true);
		this.setEnabled(true);
        this.setCredentialsNonExpired(true);
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<SprouthGrantedAuthority> authorities;
	
}
