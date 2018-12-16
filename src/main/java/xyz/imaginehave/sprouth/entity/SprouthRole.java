package xyz.imaginehave.sprouth.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

import lombok.Data;

@Entity
@Data
public class SprouthRole {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
	@Version
	private int version;
	
    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private Collection<SprouthUser> users;
 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "authority_id", referencedColumnName = "id"))
    private Collection<SprouthGrantedAuthority> authorities; 
    
    public SprouthRole() {
    	this.users = new ArrayList<>();
    	this.authorities = new HashSet<>();
    }
    
    public SprouthRole(String name) {
		this.name = name;
		this.authorities = new HashSet<>();
	}
    
    @Override
    public String toString() {
    	String authorities = "";
    	
    	for(SprouthGrantedAuthority authority : getAuthorities()) {
    		authorities += ", " + authority;
    	}
    	
    	return "SprouthRole(" + "name=" + this.getName() + authorities +")";
    }

}
