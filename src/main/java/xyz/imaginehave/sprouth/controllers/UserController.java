package xyz.imaginehave.sprouth.controllers;


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
import xyz.imaginehave.sprouth.service.SprouthSendMailService;

@RestController
@RequestMapping("/sprouth")
@Slf4j
public class UserController {

	@Autowired
    private SprouthUserRepository sprouthSprouthUserRepository;
	
	@Autowired
    private SprouthRoleRepository sprouthRoleRespository;
	
	@Autowired
    private SprouthVerficationTokenRepository sprouthVerficationTokenRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	SprouthSendMailService sprouthSendMail;
	
    @PostMapping("/sign-up")
    public void signUp(@RequestBody SprouthUser user) {
    	
    	if(sprouthSprouthUserRepository.findByUsername(user.getUsername()).isPresent()) {
    		throw new DuplicateKeyException("username already in use: " + user.getUsername());
    	}
    	
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Optional<SprouthRole> role = sprouthRoleRespository.findByName("USER");
        
        if(role.isPresent()) {
        	user.getRoles().add(role.get());
        }

        sprouthSprouthUserRepository.save(user);
        SprouthVerificationToken sprouthVerificationToken = createToken(user);
        
        log.info("New user added");
        
        sprouthSendMail.sendVerificationMail(sprouthVerificationToken);
        
        log.info("Mail sent");
    }
    
    
    @PostMapping("/verify-email")
    public void verifyEmail(@RequestBody SprouthVerificationToken token) {
    	
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
        sprouthSprouthUserRepository.save(user);
        log.info("New user registered");
        sprouthVerficationTokenRepository.delete(verificationToken);
    }
    
    
    @PostMapping("/resendVerification")
    public void resendVerification(@RequestBody SprouthUser user) {
    	
    	Optional<SprouthUser> sprouthUser = sprouthSprouthUserRepository.findByUsername(user.getUsername());
    	
    	if(!sprouthUser.isPresent()) {
    		throw new NoUserExistsException("That username does not exist: " + user.getUsername());
    	}
    	
    	Optional<SprouthVerificationToken> verificationToken = sprouthVerficationTokenRepository.findByUser(sprouthUser.get());
    	
    	if(verificationToken.isPresent()) {
    		sprouthVerficationTokenRepository.delete(verificationToken.get());
    	}
    	
    	SprouthVerificationToken sprouthVerificationToken = createToken(sprouthUser.get());
        
        sprouthSendMail.sendVerificationMail(sprouthVerificationToken);
        
        log.info("Verification resent");
    }


	private SprouthVerificationToken createToken(SprouthUser user) {
		SprouthVerificationToken sprouthVerificationToken = new SprouthVerificationToken(user);
        sprouthVerficationTokenRepository.save(sprouthVerificationToken);
        return sprouthVerificationToken;
	}
}