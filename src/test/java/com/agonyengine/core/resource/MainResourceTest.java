package com.agonyengine.core.resource;

import com.agonyengine.core.resource.model.AccountRegistration;
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
import java.security.Principal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MainResourceTest {
    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @Mock
    private Errors errors;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private AccountRegistration registration;

    @Captor
    private ArgumentCaptor<UserDetails> userDetailsCaptor;

    private MainResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(principal.getName()).thenReturn("Scion");

        resource = new MainResource(userDetailsManager);
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
        when(registration.getUsername()).thenReturn("Frank");
        when(registration.getPassword()).thenReturn("Underwood");
        when(registration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(false);
        when(errors.hasErrors()).thenReturn(false);

        String view = resource.register(registration, errors, model);

        assertEquals("login", view);

        verify(userDetailsManager).userExists(eq("Frank"));
        verify(model).addAttribute(eq("username"), eq("Frank"));
        verify(userDetailsManager).createUser(userDetailsCaptor.capture());

        UserDetails userDetails = userDetailsCaptor.getValue();

        assertEquals("Frank", userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testRegisterDuplicateUser() {
        when(registration.getUsername()).thenReturn("Frank");
        when(registration.getPassword()).thenReturn("Underwood");
        when(registration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(true);
        when(errors.hasErrors()).thenReturn(false);

        String view = resource.register(registration, errors, model);

        assertEquals("register", view);

        verify(userDetailsManager).userExists(eq("Frank"));
        verify(model).addAttribute(eq("errorText"), anyString());
        verifyNoMoreInteractions(userDetailsManager);
    }

    @Test
    public void testRegisterError() {
        ObjectError objectError = new ObjectError("username", "Username is too short.");

        when(registration.getUsername()).thenReturn("");
        when(registration.getPassword()).thenReturn("Underwood");
        when(registration.getPasswordConfirm()).thenReturn("Underwood");
        when(userDetailsManager.userExists(eq("Frank"))).thenReturn(false);
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getAllErrors()).thenReturn(Collections.singletonList(objectError));

        String view = resource.register(registration, errors, model);

        assertEquals("register", view);

        verify(model).addAttribute(eq("errorText"), anyString());
        verify(model).addAttribute(eq("username"), eq(""));
        verifyZeroInteractions(userDetailsManager);
    }

    @Test
    public void testPlay() {
        assertEquals("play", resource.play());
    }

    @Test
    public void testLogout() throws ServletException {
        assertEquals("redirect:/", resource.logout(httpServletRequest));

        verify(httpServletRequest).logout();
    }
}
