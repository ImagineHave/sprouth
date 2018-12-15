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
public class ApplicationRole {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
	@Column(unique = true)
    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private Collection<ApplicationUser> users;
 
    @ManyToMany
    @JoinTable(
        name = "application_roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    private Collection<ApplicationUser> privileges; 

}
