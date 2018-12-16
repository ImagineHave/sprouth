package xyz.imaginehave.sprouth.clr;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.entity.SprouthGrantedAuthority;
import xyz.imaginehave.sprouth.entity.SprouthRole;
import xyz.imaginehave.sprouth.repository.SprouthGrantedAuthorityRespository;
import xyz.imaginehave.sprouth.repository.SprouthRoleRepository;

@Slf4j
@Configuration
public class LoadRolesAndAuthorities {
	
	private final String GET = "GET";
	private final String POST = "POST";
	private final String DELETE = "DELETE";
	
	private final String USER = "USER";
	
	@Bean
	CommandLineRunner initDatabase(SprouthGrantedAuthorityRespository sprouthGrantedAuthorityRespository,
			SprouthRoleRepository sprouthRoleRepository) {
		return args -> {
			
			log.info("Authorities: " + sprouthGrantedAuthorityRespository.count());
			Optional<SprouthGrantedAuthority> optionalGET = sprouthGrantedAuthorityRespository.findByAuthority(GET);
			Optional<SprouthGrantedAuthority> optionalPOST = sprouthGrantedAuthorityRespository.findByAuthority(POST);
			Optional<SprouthGrantedAuthority> optionalDELETE = sprouthGrantedAuthorityRespository.findByAuthority(DELETE);
			

			if(!optionalGET.isPresent()) {
				SprouthGrantedAuthority get = new SprouthGrantedAuthority(GET);
				log.info("Preloading GET authority: " + sprouthGrantedAuthorityRespository.save(get));
			} else {
				log.info("GET: " + optionalGET.get());
			}
			
			if(!optionalPOST.isPresent()) {
				SprouthGrantedAuthority post = new SprouthGrantedAuthority(POST);
				log.info("Preloading POST authority: " + sprouthGrantedAuthorityRespository.save(post));
			} else {
				log.info("POST: " + optionalPOST.get());
			}
			
			if(!optionalDELETE.isPresent()) {
				SprouthGrantedAuthority delete = new SprouthGrantedAuthority(DELETE);
				log.info("Preloading DELETE authority: " + sprouthGrantedAuthorityRespository.save(delete));
			} else {
				log.info("DELETE: " + optionalDELETE.get());
			}
			
			log.info("Roles: " + sprouthRoleRepository.count());
			Optional<SprouthRole> optionalUser = sprouthRoleRepository.findByName(USER);
			
			if(!optionalUser.isPresent()) {
				SprouthRole user = new SprouthRole(USER);
				log.info("Preloading USER role: " + sprouthRoleRepository.save(user));
			} else {
				log.info("USER: " + optionalUser.get().getName());
			}
			
			optionalUser = sprouthRoleRepository.findByName(USER);
			
			if(optionalUser.get().getAuthorities().size() == 0 ) {
				
				optionalGET = sprouthGrantedAuthorityRespository.findByAuthority(GET);
				optionalPOST = sprouthGrantedAuthorityRespository.findByAuthority(POST);
				optionalDELETE = sprouthGrantedAuthorityRespository.findByAuthority(DELETE);
				
				optionalUser.get().getAuthorities().add(optionalGET.get());
				optionalUser.get().getAuthorities().add(optionalPOST.get());
				optionalUser.get().getAuthorities().add(optionalDELETE.get());
				
				log.info("Preloading USER authorities: " + sprouthRoleRepository.save(optionalUser.get()));
			} else {
				log.info("USER: " + optionalUser.get());
			}

		};
	}

}
