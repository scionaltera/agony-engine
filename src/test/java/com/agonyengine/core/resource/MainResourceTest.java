package com.agonyengine.core.resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainResourceTest {
    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest httpServletRequest;

    private MainResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(principal.getName()).thenReturn("Scion");

        resource = new MainResource();
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
        assertEquals("login", resource.login());
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
