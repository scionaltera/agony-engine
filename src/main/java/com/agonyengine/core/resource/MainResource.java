package com.agonyengine.core.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MainResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainResource.class);

    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getName());
        }

        return "index";
    }

    @RequestMapping("/login")
    public String login() {
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
