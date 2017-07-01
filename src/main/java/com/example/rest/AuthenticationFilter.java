package com.example.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter
{

	@Context
	private ResourceInfo resourceInfo;

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	@Override
	public void filter(ContainerRequestContext requestContext)
	{

		Method method = resourceInfo.getResourceMethod();
		if( ! method.isAnnotationPresent(PermitAll.class))
		{
			if(method.isAnnotationPresent(DenyAll.class))
			{
				throw new ForbiddenException("Access blocked");
			}
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();

			final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

			if(authorization == null || authorization.isEmpty())
			{
				throw new NotAuthorizedException("You are not authorized");
			}
			final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

			String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			final int userId = getUserIdFromRequest(requestContext.getUriInfo().getPath());


			if(method.isAnnotationPresent(RolesAllowed.class))
			{
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
				if( ! isUserAllowed(userId, username, password, rolesSet))
				{
					throw new NotAuthorizedException("Access blocked for this user");
				}


			}
		}
	}

	private int getUserIdFromRequest(String path){
		String[] parts = path.split("/");
		try{
			return Integer.parseInt(parts[parts.length-1]);
		}catch(NumberFormatException e){			
			return 0;
		}
	}
	private boolean isUserAllowed(final int userId, String username, String password, Set<String> rolesSet)
	{
		String userRole = "AUTHORIZED_USER";
		boolean isAllowed = false;
		ArrayList<Customer> customerList = Customers.getCustomers();
		Optional<Customer> customer = customerList.stream().filter(c->c.getId()==userId).findFirst();
		if(userId!=0&&customer!=null){

			if(customer.get().getEmail().equals(username) && customer.get().getPassword().equals(password)){
				if(rolesSet.contains(userRole)){
					return true;
				}				

			}
		}
		String adminRole = "ADMIN";

		if(username.equals("admin") && password.equals("admin"))
		{			

			if(rolesSet.contains(adminRole))
			{
				return true;
			}
		}

		return isAllowed;
	}
}

