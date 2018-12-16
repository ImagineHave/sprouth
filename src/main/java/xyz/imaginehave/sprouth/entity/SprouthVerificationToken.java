package xyz.imaginehave.sprouth.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class SprouthVerificationToken {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@Version
	private int version;
     
    private String token;
   
    @OneToOne(targetEntity = SprouthUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private SprouthUser user;
    
    @Setter(AccessLevel.NONE)
    private LocalDateTime expiryDate;
    
    @Setter(AccessLevel.NONE)
    private boolean expired;
   
    
    public SprouthVerificationToken() {
		setExpiryDate();
    	this.expired = false;
    	this.token = UUID.randomUUID().toString();
	}
    
    public SprouthVerificationToken(SprouthUser user) {
		this.setUser(user);
		setExpiryDate();
    	this.expired = false;
    	this.token = UUID.randomUUID().toString();
	}

    private void setExpiryDate() {
    	this.expiryDate = LocalDateTime.now().plusDays(1);
    }
    
    public boolean isExpired() {
    	this.expired = LocalDateTime.now().isAfter(this.expiryDate);
    	return this.expired;
    }
    
    @Override
    public String toString() {
    	return "SprouthVerificationToken(" + "token=" + this.token +")";
    }
    	
}
