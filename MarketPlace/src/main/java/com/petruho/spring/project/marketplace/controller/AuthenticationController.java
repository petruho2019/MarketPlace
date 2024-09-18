package com.petruho.spring.project.marketplace.controller;

import com.petruho.spring.project.marketplace.error.FieldException;
import com.petruho.spring.project.marketplace.request.SignInRequest;
import com.petruho.spring.project.marketplace.request.SignUpRequest;
import com.petruho.spring.project.marketplace.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {


    private AuthenticationService authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {

        model.addAttribute("signUp",new SignUpRequest());
        return "sign-up";

    }

    @GetMapping("/sign-in")
    public String signIn(Model model) {

        model.addAttribute("signIn", new SignInRequest());

        return "sign-in";
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult bindingResult) {

        if(signUpRequest.getEmail() == null || signUpRequest.getPassword() == null || signUpRequest.getUsername() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        authenticationService.signUp(signUpRequest);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest signInRequest, BindingResult bindingResult, HttpServletRequest request) {

        if (signInRequest.getUsername() == null && signInRequest.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (bindingResult.hasErrors()) {
            throw new FieldException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        authenticationService.signIn(signInRequest, request);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/sign-out")
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
