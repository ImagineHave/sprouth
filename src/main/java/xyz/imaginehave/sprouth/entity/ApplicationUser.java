package xyz.imaginehave.sprouth.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class ApplicationUser {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
	@Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean tokenExpired;

    
    @ManyToMany
    @JoinTable( 
        name = "application_users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<ApplicationRole> roles;
}
