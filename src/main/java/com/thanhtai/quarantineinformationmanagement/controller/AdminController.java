package com.thanhtai.quarantineinformationmanagement.controller;

import com.thanhtai.quarantineinformationmanagement.api.AdminApi;
import com.thanhtai.quarantineinformationmanagement.api.model.CreateQIRequestModel;
import com.thanhtai.quarantineinformationmanagement.api.model.LoginRequestModel;
import com.thanhtai.quarantineinformationmanagement.api.model.LoginResponse;
import com.thanhtai.quarantineinformationmanagement.api.model.ObjectCreationSuccessResponse;
import com.thanhtai.quarantineinformationmanagement.config.JwtTokenProvider;
import com.thanhtai.quarantineinformationmanagement.model.QuarantineInformation;
import com.thanhtai.quarantineinformationmanagement.service.QuarantineInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AdminController implements AdminApi {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private QuarantineInformationService quarantineInformationService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/")
//    @CrossOrigin("*")
    public String test() {
        return "Test";
    }

    @Override
    @CrossOrigin("*")
    public ResponseEntity<ObjectCreationSuccessResponse> createQuarantineInformation(@Valid CreateQIRequestModel createQIRequestModel) {
        QuarantineInformation quarantineInformation = quarantineInformationService.createQuarantineInformation(createQIRequestModel);
        ObjectCreationSuccessResponse response = new ObjectCreationSuccessResponse();
        response.setId(quarantineInformation.getId());
        response.setResponseCode(HttpStatus.CREATED.value());
        response.setMessage("Created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @CrossOrigin("*")
    public ResponseEntity<LoginResponse> login(@Valid LoginRequestModel loginRequestModel) {
        try{
            logger.info("before auth");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestModel.getUsername(),
                            loginRequestModel.getPassword()
                    )
            );
            logger.info("authentication "+authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("after set security ");
            String jwt = jwtTokenProvider.generateToken(authentication);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(jwt);
            loginResponse.setTokenType("Bearer");
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("Logging: "+e.getMessage());
        }
    }
}
