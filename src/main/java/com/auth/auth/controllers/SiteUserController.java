package com.auth.auth.controllers;

import com.auth.auth.models.SiteUser;
import com.auth.auth.repos.SiteUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class SiteUserController {
    @Autowired
    SiteUserRepository siteUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;


    @GetMapping("/")
    public String getHomePage(Model m, Principal p){
        //get principal to display user info

        if (p != null) {
            String username = p.getName();
            SiteUser user = siteUserRepository.findByUsername(username);

            m.addAttribute("username", username);
        }

        return "index.html";
    }

    //Make a dummy login page
    @GetMapping()
    public String getLoginPage() {
        return "/login.html";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password) {
        SiteUser newUser = new SiteUser();
        newUser.setUserName(username);
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);

        siteUserRepository.save(newUser);
        authWithHttpServletRequest(username, password);

        return new RedirectView("/");

    }

    public void authWithHttpServletRequest (String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("Error while logging in");
            e.printStackTrace();
        }

    }
}
