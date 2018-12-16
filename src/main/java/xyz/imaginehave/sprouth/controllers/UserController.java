package xyz.imaginehave.sprouth.controllers;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.controllers.exceptions.DuplicateKeyException;
import xyz.imaginehave.sprouth.entity.SprouthRole;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.repository.SprouthRoleRepository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private SprouthUserRepository sprouthApplicationUserRepository;
    private SprouthRoleRepository sprouthRoleRespository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(SprouthUserRepository sprouthApplicationUserRepository, 
			SprouthRoleRepository sprouthRoleRespository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.sprouthApplicationUserRepository = sprouthApplicationUserRepository;
		this.sprouthRoleRespository = sprouthRoleRespository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


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
        log.info("New user added: ", user);
    }
}