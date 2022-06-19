package com.example.nosqldb.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private Logger logger = LogManager.getLogger(CRUDControllers.class);

    /*protected String authName(Authentication auth) {

        //SecurityContext context = SecurityContextHolder.getContext();
        //Authentication authentication = context.getAuthentication();

        String userAuth = auth.getName();
        Object principal = auth.getPrincipal();

        return userAuth;
    }

    protected Authentication getAuth() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }*/

 //   @Autowired
 //   private AuthenticationManager authenticationManager;


    @GetMapping("/default-user")
    public String defaultuser() {


        return "index";
    }

    @PostMapping("/login")
    public String login() {

        //Authentication auth = getAuth();

        //String username = authName(auth);
        logger.info("in servlet path ");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
