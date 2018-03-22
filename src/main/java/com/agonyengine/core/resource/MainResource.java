package com.agonyengine.core.resource;

import com.agonyengine.core.resource.model.AccountRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainResource.class);

    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;

    @Inject
    public MainResource(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getName());
        }

        return "index";
    }

    @RequestMapping("/public/privacy")
    public String privacy() {
        return "privacy";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorText", "Invalid credentials.");
        }

        return "login";
    }

    @RequestMapping("/login/new")
    public String register() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login/new")
    public String register(@Valid AccountRegistration registration, Errors errors, Model model) {
        List<String> errorText = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> errorText.add(error.getDefaultMessage()));
        }

        if (!errorText.isEmpty()) {
            model.addAttribute("errorText", String.join("<br />", errorText));
            model.addAttribute("username", registration.getUsername());

            return "register";
        }

        if (userDetailsManager.userExists(registration.getUsername())) {
            model.addAttribute("errorText", "That username is not available. Please try another one.");

            return "register";
        }

        UserDetails user = new User(
            registration.getUsername(),
            passwordEncoder.encode(registration.getPassword()),
            Collections.singletonList(new SimpleGrantedAuthority("USER")));

        userDetailsManager.createUser(user);

        LOGGER.info("New user created: {}", user.getUsername());

        model.addAttribute("username", user.getUsername());

        return "login";
    }

    @RequestMapping("/play")
    public String play() {
        return "play";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();

        LOGGER.info("Logged out.");

        return "redirect:/";
    }
}
