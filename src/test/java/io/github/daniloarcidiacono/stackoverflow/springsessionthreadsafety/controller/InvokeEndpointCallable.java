package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.Callable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Callable that invokes a specified endpoint.
 * The return value is equal to 1 if the response is 200 OK, 0 otherwise.
 */
public class InvokeEndpointCallable implements Callable<Integer> {
	private String url;
	private String username, password;
	private MockMvc mockMvc;

	public InvokeEndpointCallable(final String url, final String username, final String password, final MockMvc mockMvc) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.mockMvc = mockMvc;
	}

	@Override
	public Integer call() throws Exception {
		final MvcResult mvcResult = mockMvc.perform(
			get(url)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + SecurityUtils.basicAuthentication(username, password))
		).andReturn();

		return mvcResult.getResponse().getStatus() == HttpStatus.OK.value() ? 1 : 0;
	}
}