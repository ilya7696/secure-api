package by.tovpenets.secure.api.service;

import by.tovpenets.secure.api.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return CustomUserDetails.of(userService.findByLogin(username));
    }
}
