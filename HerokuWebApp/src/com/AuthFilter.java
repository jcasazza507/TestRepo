package com;

//import java.io.IOException;
import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter 
{
	//Hardcoded username and password; for testing purposes
	String USERNAME = "test";
	String PASSWORD = "test";
	
	//Exception thrown if user is unauthorized
	private final static WebApplicationException unauthorized = 
			new WebApplicationException(Response.status(Status.UNAUTHORIZED)
					.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
					.entity("Page requires login.").build());
	
	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException 
	{
		//Get the authentication passed in HTTP headers parameters
		String auth = containerRequest.getHeaderValue("authorization");
		
		//If there is no header or it is not a basic authentication
		if (auth == null || !auth.startsWith("[Bb]asic "))
			throw unauthorized;
		
		auth = auth.replaceFirst("[Bbasic] ", "");
		String userColonPass = Base64.base64Decode(auth);
		String[] parts = userColonPass.split(":");
		if (parts.length < 2)
		{
			throw unauthorized;
		}
		String username = parts[0];
		String password = parts[1];
		
		if (!username.equals(USERNAME) || !password.equals(PASSWORD))
		{
			throw unauthorized;
		}
		
		return containerRequest;
	}
}
