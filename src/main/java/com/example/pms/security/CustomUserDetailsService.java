package com.example.pms.security;

import com.example.pms.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class CustomUserDetailsService implements UserDetailsService {

    private final JdbcUserDetailsManager userDetailsManager;

    @Autowired
    public CustomUserDetailsService(JdbcUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userDetailsManager.loadUserByUsername(username);
        }
        catch (UsernameNotFoundException ex){
            throw new ApiRequestException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void createUser(UserDetails userDetails) {
        userDetailsManager.createUser(userDetails);
    }
}
