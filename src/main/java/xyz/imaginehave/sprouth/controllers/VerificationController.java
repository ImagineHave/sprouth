package xyz.imaginehave.sprouth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.controllers.exceptions.VerificationTokenDoesNotExistException;
import xyz.imaginehave.sprouth.controllers.exceptions.VerificationTokenExpired;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;
import xyz.imaginehave.sprouth.repository.SprouthVerficationTokenRepository;

@Controller
@Slf4j
public class VerificationController {
	
	@Autowired
    private SprouthVerficationTokenRepository sprouthVerficationTokenRepository;
	
	@Autowired
    private SprouthUserRepository sprouthApplicationUserRepository;
    
    @GetMapping("/sprouth/verify-email")
    @ResponseBody
    public String verifyEmail(String code) {
    	
    	if(!sprouthVerficationTokenRepository.findByToken(code).isPresent()) {
    		throw new VerificationTokenDoesNotExistException("Token does not exist: " + code);
    	}
    	
    	SprouthVerificationToken verificationToken = sprouthVerficationTokenRepository.findByToken(code).get();
        
    	if(verificationToken.isExpired()) {
    		sprouthVerficationTokenRepository.delete(verificationToken);
    		throw new VerificationTokenExpired("Token expired: " + verificationToken);
    	}

    	SprouthUser user = verificationToken.getUser();

    	user.setEnabled(true);
        sprouthApplicationUserRepository.save(user);
        log.info("New user registered");
        sprouthVerficationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok("You have successfully verified your email address.").getBody();
    }
}