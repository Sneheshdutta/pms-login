package com.cognizant.pms.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.pms.login.apiRequest.AuthenticationRequest;
import com.cognizant.pms.login.apiResponse.ApiResponse;
import com.cognizant.pms.login.service.UserService;
import com.cognizant.pms.login.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/login")
@Slf4j
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Entered createAuthenticationToken()");
        try {
        	if (!userService.authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword()))
            {
        		log.error("Incorrect Credentials");
        		ResponseEntity<ApiResponse> response = ResponseEntity.ok(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Incorrect Credentials", null));
        		log.info("response:"+response);
                return response;
            }
        }catch(Exception e) {
        	log.error(e.getMessage());
    		ResponseEntity<ApiResponse> response = ResponseEntity.ok(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null));
    		log.info("response:"+response);
            return response;
        }
    	
        final String token = jwtUtil.generateToken(authenticationRequest.getUserName());
        log.info("token:"+token);
        ResponseEntity<ApiResponse> response = ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Login Successfull", token));
        log.info("response:"+response);
        log.info("Exiting createAuthenticationToken()");
        return response;
    }



    @PostMapping("/register")
    public ResponseEntity<ApiResponse> signUp(@RequestBody AuthenticationRequest authenticationRequest) {
    	log.info("Entered signUp()");
    	try {
    		ResponseEntity<ApiResponse> response =  ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.OK.value()).message("User Registered Successfully").data(userService.createNewAccount(authenticationRequest)).build());
    		log.info("response:"+response);
            return response;
    	}catch(Exception e) {
    		log.error(e.getMessage());
    		ResponseEntity<ApiResponse> response = ResponseEntity.ok(new ApiResponse(HttpStatus.CONFLICT.value(), e.getMessage(), null));
    		log.info("response:"+response);
    		log.info("Exiting signUp()");
            return response;
    	}
    	
        
    }
    
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader("Authorization") String authToken) {
    	try {
    		return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),"Token Validated",jwtUtil.validateToken(authToken)));
    	}catch(Exception e) {
    		return ResponseEntity.ok(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),"Invalid Token",false));
    	}
    }

}
