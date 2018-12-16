package xyz.imaginehave.sprouth.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
public class SprouthVerificationToken {
	
    private static final int EXPIRATION = 60 * 24;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     
    private String token;
   
    @OneToOne(targetEntity = SprouthUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private SprouthUser user;
    
    @Setter(AccessLevel.NONE)
    private Date expiryDate;
    
    @Setter(AccessLevel.NONE)
    private boolean expired;
    
    public SprouthVerificationToken() {
    	setExpiryDate();
    	this.expired = false;
    }
    
    public SprouthVerificationToken(SprouthUser user) {
		this.setUser(user);
		setExpiryDate();
    	this.expired = false;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    private void setExpiryDate() {
    	this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    
    public boolean isExpired() {
    	Calendar cal = Calendar.getInstance();
    	this.expired = (this.expiryDate.getTime() - cal.getTime().getTime()) <= 0;
    	return this.expired;
    }
    	
}
