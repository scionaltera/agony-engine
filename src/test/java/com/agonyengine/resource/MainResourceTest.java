package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Pronoun;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.PronounRepository;
import com.agonyengine.resource.model.AccountRegistration;
import com.agonyengine.resource.model.PlayerActorRegistration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MainResourceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private PronounRepository pronounRepository;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private Actor actor;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @Mock
    private Errors errors;

    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private AccountRegistration accountRegistration;

    @Mock
    private PlayerActorRegistration actorRegistration;

    @Mock
    private Pronoun pronoun;

    @Captor
    private ArgumentCaptor<UserDetails> userDetailsCaptor;

    @Captor
    private ArgumentCaptor<Actor> actorCaptor;

    private MainResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actorRepository.save(any(Actor.class))).thenAnswer(i -> {
            Actor actor = i.getArgument(0);

            actor.setId(UUID.randomUUID());

            return actor;
        });

        when(pronounRepository.getOne(eq("he"))).thenReturn(pronoun);

        resource = new MainResource(actorRepository, pronounRepository, userDetailsManager);
    }

    @Test
    public void testIndexPublic() {
        assertEquals("index", resource.index(null, model));
    }

    @Test
    public void testIndexAuthenticated() {
        assertEquals("index", resource.index(principal, model));
    }

    @Test
    public void testPrivacy() {
        assertEquals("privacy", resource.privacy());
    }

    @Test
    public void testLogin() {
        String view = resource.login(null, model);

        assertEquals("login", view);

        verify(model, never()).addAttribute(eq("errorText"), anyString());
    }

    @Test
    public void testLoginError() {
        String view = resource.login("Oops", model);

        assertEquals("login", view);

        verify(model).addAttribute(eq("errorText"), anyString());
    }

    @Test
    public void testRegisterGet() {
        assertEquals("register", resource.register());
    }

    @Test
    public void testRegisterValid() {
        when(accountRegistration.getUsername()).thenReturn("Frank");
        when(accountRegistration.getPassword()).thenReturn("Underwood");
        when(accountRegistration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(false);
        when(errors.hasErrors()).thenReturn(false);

        String view = resource.register(accountRegistration, errors, model);

        assertEquals("login", view);

        verify(model).addAttribute(eq("username"), eq("Frank"));
        verify(userDetailsManager).createUser(userDetailsCaptor.capture());

        UserDetails userDetails = userDetailsCaptor.getValue();

        assertEquals("Frank", userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testRegisterDuplicateUser() {
        ObjectError objectError = new ObjectError("username", "That username is not available.");

        when(accountRegistration.getUsername()).thenReturn("Frank");
        when(accountRegistration.getPassword()).thenReturn("Underwood");
        when(accountRegistration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(true);
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getAllErrors()).thenReturn(Collections.singletonList(objectError));

        String view = resource.register(accountRegistration, errors, model);

        assertEquals("register", view);

        verify(model).addAttribute(eq("errorText"), anyString());
        verify(model).addAttribute(eq("username"), eq("Frank"));
        verifyZeroInteractions(userDetailsManager);
    }

    @Test
    public void testRegisterError() {
        ObjectError objectError = new ObjectError("username", "Username is too short.");

        when(accountRegistration.getUsername()).thenReturn("");
        when(accountRegistration.getPassword()).thenReturn("Underwood");
        when(accountRegistration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(false);
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getAllErrors()).thenReturn(Collections.singletonList(objectError));

        String view = resource.register(accountRegistration, errors, model);

        assertEquals("register", view);

        verify(model).addAttribute(eq("errorText"), anyString());
        verify(model).addAttribute(eq("username"), eq(""));
        verifyZeroInteractions(userDetailsManager);
    }

    @Test
    public void testAccount() {
        List<Actor> patList = new ArrayList<>();

        when(principal.getName()).thenReturn("Shepherd");
        when(actorRepository.findByAccount(eq("Shepherd"))).thenReturn(patList);

        String view = resource.account(principal, model);

        assertEquals("account", view);

        verify(actorRepository).findByAccount(eq("Shepherd"));
        verify(model).addAttribute(eq("actors"), eq(patList));
    }

    @Test
    public void testActorNew() {
        assertEquals("actor", resource.actor(model));
    }

    @Test
    public void testActorValid() {
        when(principal.getName()).thenReturn("Shepherd");
        when(actorRegistration.getGivenName()).thenReturn("Frank");
        when(actorRegistration.getPronoun()).thenReturn("he");
        when(errors.hasErrors()).thenReturn(false);

        String view = resource.actor(actorRegistration, errors, principal, model);

        assertEquals("redirect:/account", view);

        verify(errors).hasErrors();
        verify(actorRepository).save(actorCaptor.capture());

        Actor actor = actorCaptor.getValue();

        assertNotNull(actor.getId());
        assertEquals("Frank", actor.getName());
        assertEquals(pronoun, actor.getPronoun());
        assertEquals("Shepherd", actor.getAccount());
    }

    @Test
    public void testActorError() {
        ObjectError objectError = new ObjectError("givenName", "Given name is too short.");

        when(actorRegistration.getGivenName()).thenReturn("");
        when(actorRegistration.getPronoun()).thenReturn("he");
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getAllErrors()).thenReturn(Collections.singletonList(objectError));

        String view = resource.actor(actorRegistration, errors, principal, model);

        assertEquals("actor", view);

        verify(model).addAttribute(eq("errorText"), anyString());
        verify(model).addAttribute(eq("givenName"), eq(""));
        verifyZeroInteractions(actorRepository);
    }

    @Test
    public void testPlay() {
        UUID actorId = UUID.randomUUID();

        when(httpSession.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(actor));
        when(actor.getId()).thenReturn(actorId);
        when(actor.getAccount()).thenReturn("Shepherd");
        when(principal.getName()).thenReturn("Shepherd");

        String view = resource.play(principal, httpServletRequest, model, httpSession);

        assertEquals("play", view);

        verify(httpSession).setAttribute(eq("remoteIpAddress"), any());
        verify(httpSession).removeAttribute(eq("actor"));

        verify(model).addAttribute(eq("actor"), eq(actorId.toString()));
    }

    @Test
    public void testPlayActorNotFound() {
        UUID actorId = UUID.randomUUID();

        when(httpSession.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(actorId)).thenReturn(Optional.empty());
        when(actor.getId()).thenReturn(actorId);
        when(actor.getAccount()).thenReturn("Shepherd");
        when(principal.getName()).thenReturn("Shepherd");

        String view = resource.play(principal, httpServletRequest, model, httpSession);

        assertEquals("redirect:/account", view);

        verify(httpSession, never()).setAttribute(eq("remoteIpAddress"), any());
        verify(httpSession, never()).removeAttribute(eq("actor"));

        verify(model, never()).addAttribute(eq("actor"), eq(actorId.toString()));
    }

    @Test
    public void testPlayActorWrongUser() {
        UUID actorId = UUID.randomUUID();

        when(httpSession.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(actor));
        when(actor.getId()).thenReturn(actorId);
        when(actor.getAccount()).thenReturn("Scion");
        when(principal.getName()).thenReturn("Shepherd");

        String view = resource.play(principal, httpServletRequest, model, httpSession);

        assertEquals("redirect:/account", view);

        verify(httpSession, never()).setAttribute(eq("remoteIpAddress"), any());
        verify(httpSession, never()).removeAttribute(eq("actor"));

        verify(model, never()).addAttribute(eq("actor"), eq(actorId.toString()));
    }

    @Test
    public void testPlayId() {
        String view = resource.play("actor-id", httpSession);

        verify(httpSession).setAttribute(eq("actor"), eq("actor-id"));

        assertEquals("redirect:/play", view);
    }

    @Test
    public void testLogout() throws ServletException {
        assertEquals("redirect:/", resource.logout(httpServletRequest));

        verify(httpServletRequest).logout();
    }
}
