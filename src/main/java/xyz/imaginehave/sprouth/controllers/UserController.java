package xyz.imaginehave.sprouth.controllers;


import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.controllers.exceptions.DuplicateKeyException;
import xyz.imaginehave.sprouth.controllers.exceptions.NoUserExistsException;
import xyz.imaginehave.sprouth.controllers.exceptions.VerificationTokenDoesNotExistException;
import xyz.imaginehave.sprouth.controllers.exceptions.VerificationTokenExpired;
import xyz.imaginehave.sprouth.entity.SprouthRole;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;
import xyz.imaginehave.sprouth.repository.SprouthRoleRepository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;
import xyz.imaginehave.sprouth.repository.SprouthVerficationTokenRepository;
import xyz.imaginehave.sprouth.service.SprouthSendMail;
import xyz.imaginehave.sprouth.service.SprouthVerificationTokenService;

@RestController
@RequestMapping("/sprouth")
@Slf4j
public class UserController {

	@Autowired
    private SprouthUserRepository sprouthApplicationUserRepository;
	
	@Autowired
    private SprouthRoleRepository sprouthRoleRespository;
	
	@Autowired
    private SprouthVerficationTokenRepository sprouthVerficationTokenRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	SprouthVerificationTokenService sprouthVerificationTokenService;
	
	@Autowired
	SprouthSendMail sprouthSendMail;
	
    @PostMapping("/sign-up")
    public void signUp(@RequestBody SprouthUser user) {
    	
    	if(sprouthApplicationUserRepository.findByUsername(user.getUsername()).isPresent()) {
    		throw new DuplicateKeyException("username already in use: " + user.getUsername());
    	}
    	
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Optional<SprouthRole> role = sprouthRoleRespository.findByName("USER");
        
        if(role.isPresent()) {
        	user.getRoles().add(role.get());
        }

        sprouthApplicationUserRepository.save(user);
        sprouthVerificationTokenService.createToken(user);
        
        log.info("New user added");
        
        
        
        try {
			sprouthSendMail.sendActivationEmail(user);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
    }
    
    
    @PostMapping("/register")
    public void register(@RequestBody SprouthVerificationToken token) {
    	
    	
    	if(!sprouthVerficationTokenRepository.findByToken(token.getToken()).isPresent()) {
    		throw new VerificationTokenDoesNotExistException("Token does not exist: " + token);
    	}
    	
    	SprouthVerificationToken verificationToken = sprouthVerficationTokenRepository.findByToken(token.getToken()).get();
    	
    	if(verificationToken.isExpired()) {
    		sprouthVerficationTokenRepository.delete(verificationToken);
    		throw new VerificationTokenExpired("Token expired: " + verificationToken);
    	}
    	
    	SprouthUser user = verificationToken.getUser();

    	user.setEnabled(true);
        sprouthApplicationUserRepository.save(user);
        log.info("New user registered");
        sprouthVerficationTokenRepository.delete(verificationToken);
    }
    
    
    @PostMapping("/resendVerification")
    public void resendVerification(@RequestBody SprouthUser user) {
    	
    	if(!sprouthApplicationUserRepository.findByUsername(user.getUsername()).isPresent()) {
    		throw new NoUserExistsException("That username does not exist: " + user.getUsername());
    	}
    	
    	Optional<SprouthVerificationToken> verificationToken = sprouthVerficationTokenRepository.findByUser(user);
    	
    	if(verificationToken.isPresent()) {
    		sprouthVerficationTokenRepository.delete(verificationToken.get());
    	}
    	
        sprouthVerificationTokenService.createToken(user);
        
        log.info("New user added");
    }
}