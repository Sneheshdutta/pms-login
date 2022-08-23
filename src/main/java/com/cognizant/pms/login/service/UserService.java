package com.cognizant.pms.login.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cognizant.pms.login.apiRequest.AuthenticationRequest;
import com.cognizant.pms.login.exception.LoginException;
import com.cognizant.pms.login.model.User;
import com.cognizant.pms.login.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Component("userDetailsImpl")
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public User createNewAccount(AuthenticationRequest request) throws LoginException {


        String textPassword = request.getPassword();
        String encryptedPassword = passwordEncoder.encode(textPassword);
        String userName = request.getUserName();

        User user = User.builder().userName(userName).password(encryptedPassword).build();

        Optional<User> foundByUsername = userRepository.findByUserName(user.getUserName());
        if (foundByUsername.isPresent()) {
            throw new LoginException("Username - " + foundByUsername.get().getUserName()
                    + " has already been registered with our system. Please use another one.");
        }
        return userRepository.saveAndFlush(user);
    }

    public boolean authenticate(String username, String password) throws LoginException {
    	log.info("Entered authenticate() for username:"+username);
        Optional<User> user = userRepository.findByUserName(username);
        if(!user.isPresent()){
        	log.error("Username not found");
            throw new LoginException("Username not found");
        }
        else {
        	log.info("Exiting authenticate()");
        	return passwordEncoder.matches(password, user.get().getPassword());
        }
    }
}
