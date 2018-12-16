package xyz.imaginehave.sprouth.service;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;
import xyz.imaginehave.sprouth.repository.SprouthVerficationTokenRepository;

@Service
public class SprouthSendMail {

    @Autowired 
    it.ozimov.springboot.mail.service.EmailService emailService; 
    
    @Autowired
    SprouthVerficationTokenRepository sprouthVerficationTokenRepository;

    @Value("${spring.mail.username}") 
    String fromEmail;
	
    public void sendActivationEmail(SprouthUser user) throws UnsupportedEncodingException {
    	
    	Optional<SprouthVerificationToken> sprouthVerficationToken = sprouthVerficationTokenRepository.findByUser(user);
    	
    	if(sprouthVerficationToken.isPresent()) {
    	
	        final Email email = DefaultEmail.builder() 
	            .from(new InternetAddress(fromEmail, "no-reply"))
	            .to(Lists.newArrayList(new InternetAddress(
	            		user.getEmail(), user.getUsername()))) 
	            .subject("Testing email")
	            .body(sprouthVerficationToken.get().getToken())
	            .encoding("UTF-8").build();
	        emailService.send(email); 
    	}
    }

}
