package com.auth.auth.controllers;

import com.auth.auth.models.SiteUser;
import com.auth.auth.repos.SiteUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password) {
        SiteUser newUser = new SiteUser();
        newUser.setUsername(username);
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

    @GetMapping("/test")
    public String getTestPage(Model m, Principal p) {
        if (p != null){
            String username = p.getName();
            SiteUser user = siteUserRepository.findByUsername(username);

            if(user != null)
                m.addAttribute("username", user.getUsername());
        }
        return "test.html";
    }

    @GetMapping("/user/{id}")
    public String getUserInfoPage(Model m, Principal p, @PathVariable long id) {
        if (p != null) {
            String username = p.getName();
            SiteUser browsingUser = siteUserRepository.findByUsername(username);
            m.addAttribute("username", browsingUser.getUsername());
        }

        SiteUser profileUser = siteUserRepository.findById(id).orElseThrow();
        m.addAttribute("profileUsername", profileUser.getUsername());
        m.addAttribute("profileId",profileUser.getId());
        m.addAttribute("profileDateCreated", profileUser.getDateCreated());

        return "user-info.html";
    }

    @PutMapping("/user/{id}")
    public RedirectView updateUserInfo(Model m, Principal p, @PathVariable Long id, String profileUsername,
                                       RedirectAttributes redir) {
        SiteUser userToBeEdited = siteUserRepository.findById(id).orElseThrow();

        if(p != null && p.getName().equals(userToBeEdited.getUsername())) {
            userToBeEdited.setUsername(profileUsername);
            siteUserRepository.save(userToBeEdited);

            // include lines below if your principal is not updating
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userToBeEdited, userToBeEdited.getPassword(),
                    userToBeEdited.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            redir.addFlashAttribute("errorMessage", "Can not edit another user's page!");
        }

        return new RedirectView("/user/"+id);
    }
}
