package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SimpleControllerTest {
    private static final String SESSION_COOKIE_NAME = "SESSION";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void springSecurityWorks() throws Exception {
        mockMvc.perform(
            get("/simple/whoami")
        ).andExpect(
            status().is(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @Test
    public void springSessionWorks() throws Exception {
        final String username = "user";
        final String password = "password";

        // Create a new session
        final MvcResult result = mockMvc.perform(
            get("/simple/whoami")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SecurityUtils.basicAuthentication(username, password))
        ).andExpect(
            status().is(HttpStatus.OK.value())
        ).andExpect(
            content().string("user")
        ).andExpect(
            cookie().exists(SESSION_COOKIE_NAME)
        ).andReturn();

        // Use the session
        final Cookie sessionCookie = result.getResponse().getCookie(SESSION_COOKIE_NAME);
        mockMvc.perform(
            get("/simple/whoami")
                .cookie(sessionCookie)
        ).andExpect(
            status().is(HttpStatus.OK.value())
        ).andExpect(
            content().string("user")
        );
    }
}