package xyz.imaginehave.sprouth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

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
        Optional<SprouthUser> applicationUser = applicationUserRepository.findByUsername(username);
        if (!applicationUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return applicationUser.get();
    }
}
