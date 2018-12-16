package xyz.imaginehave.sprouth.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Data
public class SprouthUser implements UserDetails {
    
	private static final long serialVersionUID = 27584725850867301L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Version
	private int version;
    
	@Column(unique = true, nullable = false)
    private String username;
	
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    @Column
    private String firstName;
    
    @Column
    private String lastName;
    
    @Column
	private boolean accountNonExpired;
	
    @Column
	private boolean accountNonLocked;
	
    @Column
	private boolean credentialsNonExpired;
	
    @Column
	private boolean enabled;
	

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Set<SprouthRole> roles;
	
	public SprouthUser() {
		this.setRoles(new HashSet<>());
		this.setAccountNonExpired(true);
		this.setAccountNonLocked(true);
		this.setEnabled(false);
        this.setCredentialsNonExpired(true);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for(SprouthRole role: roles) {
			authorities.addAll(role.getAuthorities());
		}
		return authorities;
	}

	
}
