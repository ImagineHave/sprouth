package xyz.imaginehave.sprouth.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class RefreshToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Version
	private int version;
	
    @OneToOne(targetEntity = SprouthUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @JsonIgnore
    private SprouthUser user;
	
	private String token;
	
    @Setter(AccessLevel.NONE)
    private LocalDateTime expiryDate;
    
    @Setter(AccessLevel.NONE)
    private boolean expired;
    
    public RefreshToken() {
    	setExpiryDate();
    }
	
	public RefreshToken(SprouthUser sprouthUser) {
		this.user = sprouthUser;
		setExpiryDate();
	}
	
    private void setExpiryDate() {
    	this.expiryDate = LocalDateTime.now().plusHours(8);
    }
    
    public boolean isExpired() {
    	this.expired = LocalDateTime.now().isAfter(this.expiryDate);
    	return this.expired;
    }

}
