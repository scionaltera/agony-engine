package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.PronounRepository;
import com.agonyengine.resource.exception.NoSuchActorException;
import com.agonyengine.resource.model.AccountRegistration;
import com.agonyengine.resource.model.PlayerActorRegistration;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class MainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainResource.class);

    private ActorRepository actorRepository;
    private PronounRepository pronounRepository;
    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;

    @Inject
    public MainResource(ActorRepository actorRepository,
                        PronounRepository pronounRepository,
                        UserDetailsManager userDetailsManager) {

        this.actorRepository = actorRepository;
        this.pronounRepository = pronounRepository;
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

        UserDetails user = new User(
            registration.getUsername(),
            passwordEncoder.encode(registration.getPassword()),
            Collections.singletonList(new SimpleGrantedAuthority("USER")));

        userDetailsManager.createUser(user);

        LOGGER.info("New account created: {}", user.getUsername());

        model.addAttribute("username", user.getUsername());

        return "login";
    }

    @RequestMapping("/account")
    public String account(Principal principal, Model model) {
        List<Actor> actors = actorRepository.findByAccount(principal.getName());

        model.addAttribute("actors", actors);

        return "account";
    }

    @RequestMapping("/actor/new")
    public String actor(Model model) {
        model.addAttribute("pronouns", pronounRepository.findAll());

        return "actor";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/actor/new")
    public String actor(@Valid PlayerActorRegistration registration, Errors errors, Principal principal, Model model) {
        List<String> errorText = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> errorText.add(error.getDefaultMessage()));
        }

        if (!errorText.isEmpty()) {
            model.addAttribute("pronouns", pronounRepository.findAll());
            model.addAttribute("errorText", String.join("<br />", errorText));
            model.addAttribute("givenName", registration.getGivenName());
            model.addAttribute("selectedPronoun", registration.getPronoun());

            return "actor";
        }

        Actor actor = new Actor();

        actor.setAccount(principal.getName());
        actor.setName(registration.getGivenName());
        actor.setPronoun(pronounRepository.getOne(registration.getPronoun()));

        actor = actorRepository.save(actor);

        LOGGER.info("New character created: {}", actor.getName());

        return "redirect:/account";
    }

    @RequestMapping("/play")
    public String play(Principal principal, HttpServletRequest request, Model model, HttpSession httpSession) {
        try {
            Actor actor = actorRepository
                .findById(UUID.fromString((String) httpSession.getAttribute("actor")))
                .orElseThrow(() -> new NoSuchActorException("No Actor exists with specified ID"));

            if (!actor.getAccount().equals(principal.getName())) {
                throw new NoSuchActorException("Actor belongs to a different user");
            }

            httpSession.setAttribute("remoteIpAddress", extractRemoteIp(request));
            httpSession.removeAttribute("actor");

            model.addAttribute("actor", actor.getId().toString());

            return "play";
        } catch (NoSuchActorException e) {
            LOGGER.error("Invalid actor ID: {}", e.getMessage());
        }

        return "redirect:/account";
    }

    @RequestMapping("/play/{id}")
    public String play(@PathVariable("id") String id, HttpSession httpSession) {
        httpSession.setAttribute("actor", id);

        return "redirect:/play";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();

        LOGGER.info("Logged out.");

        return "redirect:/";
    }

    private String extractRemoteIp(HttpServletRequest request) {
        String forwardedHeader = request.getHeader("x-forwarded-for");

        if (forwardedHeader != null) {
            String[] addresses = forwardedHeader.split("[,]");

            for (String address : addresses) {
                try {
                    InetAddress inetAddress = InetAddress.getByName(address);

                    if (!inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    LOGGER.debug("Failed to resolve IP for address: {}", address);
                }
            }
        }

        return request.getRemoteAddr();
    }
}
