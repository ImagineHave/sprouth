package xyz.imaginehave.sprouth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.repository.SprouthUserRepository;

@Service
public class SprouthUserDetailsService implements UserDetailsService {
	
    private SprouthUserRepository applicationUserRepository;

    public SprouthUserDetailsService(SprouthUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SprouthUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return applicationUser;
    }
}
