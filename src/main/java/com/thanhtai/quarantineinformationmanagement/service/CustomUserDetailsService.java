package com.thanhtai.quarantineinformationmanagement.service;

import com.thanhtai.quarantineinformationmanagement.model.User;
import com.thanhtai.quarantineinformationmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.security.user.name}")
    private String USER_NAME;
    @Value("${spring.security.user.password}")
    private String PASSWORD;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (userRepository.findByUsername(username) == null) {
//            throw new UsernameNotFoundException("username-" + username);
//        }
//        User user = userRepository.findByUsername(username);
//        logger.info("user-" + user.getUsername() + " role-" + user.getRole());
        logger.info("start custom");
        if (!username.equals(USER_NAME)) {
            throw new RuntimeException("Invalid username/password");
        }
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ADMIN");
        logger.info("end custom");
        String password = passwordEncoder.encode(PASSWORD);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, Collections.singleton(auth));
        logger.info("userdetails " + userDetails.toString() + " pass " + password);
        return userDetails;
    }
}