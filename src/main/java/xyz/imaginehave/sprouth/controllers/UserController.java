package xyz.imaginehave.sprouth.controllers;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.imaginehave.sprouth.entity.SprouthGrantedAuthority;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.repository.SprouthGrantedAuthorityRespository;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private SprouthUserRepository sprouthApplicationUserRepository;
    private SprouthGrantedAuthorityRespository sprouthGrantedAuthorityRespository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserController(SprouthUserRepository sprouthApplicationUserRepository, 
			SprouthGrantedAuthorityRespository sprouthGrantedAuthorityRespository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.sprouthApplicationUserRepository = sprouthApplicationUserRepository;
		this.sprouthGrantedAuthorityRespository = sprouthGrantedAuthorityRespository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


    @PostMapping("/sign-up")
    public void signUp(@RequestBody SprouthUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Optional<SprouthGrantedAuthority> userAuthority = sprouthGrantedAuthorityRespository.findByAuthority("USER");
        if(userAuthority.isPresent()) {
        	user.getAuthorities().add(userAuthority.get());
        }
        sprouthApplicationUserRepository.save(user);
    }
}