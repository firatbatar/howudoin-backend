package edu.sabanciuniv.howudoin.controller;

import edu.sabanciuniv.howudoin.component.JwtHelperUtils;
import edu.sabanciuniv.howudoin.model.LoginModel;
import edu.sabanciuniv.howudoin.model.LoginResponse;
import edu.sabanciuniv.howudoin.model.UserModel;
import edu.sabanciuniv.howudoin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelperUtils jwtHelperUtils;

    @Autowired
    public UserController(
            UserDetailsService userDetailsService,
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtHelperUtils jwtHelperUtils
    ) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtHelperUtils = jwtHelperUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerNewUser(@RequestBody UserModel userModel) {
        UserModel newUser = this.userService.registerUser(userModel);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginModel loginModel) throws BadCredentialsException {
        this.doAuthenticate(loginModel.getEmail(), loginModel.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginModel.getEmail());
        String token = this.jwtHelperUtils.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    private void doAuthenticate(String username, String password) throws BadCredentialsException {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!");
        }
    }
}
