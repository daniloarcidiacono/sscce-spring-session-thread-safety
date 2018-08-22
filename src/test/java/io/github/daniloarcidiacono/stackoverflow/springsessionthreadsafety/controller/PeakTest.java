package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for a request burst. This is separated from {@link SimpleControllerTest} to run
 * in a pristine environment.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PeakTest {
    @Value("${peak.threads}")
    private int nThreads;

    @Value("${peak.total-requests}")
    private int totalRequests;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void requestPeak() throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        try {
            // Invoke the endpoint with the same user (without cookie, so many sessions are created)
            final List<InvokeEndpointCallable> invocations = new ArrayList<>();
            for (int i = 0; i < totalRequests; i++) {
                invocations.add(new InvokeEndpointCallable("/simple/whoami", "user", "password", mockMvc));
            }

            // Count the number of accepted calls
            int acceptedCalls = 0;
            for (Future<Integer> future : executor.invokeAll(invocations)) {
                acceptedCalls += future.get();
            }

            // This should be self-explanatory
            assertEquals("Only one session should be created", 1, acceptedCalls);
        } finally {
            executor.shutdownNow();
        }
    }
}