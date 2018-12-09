package xyz.imaginehave.sprouth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phone;
	
    
    public Contact(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

}

