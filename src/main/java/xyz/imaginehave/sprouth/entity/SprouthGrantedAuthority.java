package xyz.imaginehave.sprouth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Data
public class SprouthGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -4919742921994408255L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Version
	private int version;
	
	@Column(unique = true)
	private String authority;
	
	
	public SprouthGrantedAuthority() {

	}
	
	public SprouthGrantedAuthority(String authority) {
		this.authority = authority;
	}
	
}
