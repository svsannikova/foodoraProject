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
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
			.entity("You are not authorized").build();
	private static final Response ACCESS_DENIED_TO_USER = Response.status(Response.Status.UNAUTHORIZED)
			.entity("Access blocked for this user").build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
			.entity("Access blocked").build();

	@Override
	public void filter(ContainerRequestContext requestContext)
	{
				
		Method method = resourceInfo.getResourceMethod();
		if( ! method.isAnnotationPresent(PermitAll.class))
		{
			if(method.isAnnotationPresent(DenyAll.class))
			{
				requestContext.abortWith(ACCESS_FORBIDDEN);
				return;
			}
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();

			final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

			if(authorization == null || authorization.isEmpty())
			{
				requestContext.abortWith(ACCESS_DENIED);
				return;
			}
			final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

			String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			final Integer userId = getUserIdFromRequest(requestContext.getUriInfo().getPath());

			System.out.println(userId);
			System.out.println(username);
			System.out.println(password);

			if(method.isAnnotationPresent(RolesAllowed.class))
			{
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
				if(userId!=null){
					if( ! isUserAllowed(userId, username, password, rolesSet))
					{
						requestContext.abortWith(ACCESS_DENIED_TO_USER);
						return;
					}
				}else{
					if( ! isAdminAllowed(username, password, rolesSet))
					{
						requestContext.abortWith(ACCESS_DENIED_TO_USER);
						return;
					}
				}
				
			}
		}
	}
	
	private Integer getUserIdFromRequest(String path){
		String[] parts = path.split("/");
		try{
			return Integer.parseInt(parts[parts.length-1]);
		}catch(NumberFormatException e){			
			return null;
		}
	}
	private boolean isAdminAllowed(String username, String password, Set<String> rolesSet)
	{

		String adminRole = "ADMIN";
		boolean isAllowed = false;
		
		if(username.equals("admin") && password.equals("admin"))
		{			

			if(rolesSet.contains(adminRole))
			{
				return true;
			}
		}
		return isAllowed;
	}
	private boolean isUserAllowed(final Integer userId, String username, String password, Set<String> rolesSet)
	{
		String userRole = "AUTHORIZED_USER";
		boolean isAllowed = false;
		ArrayList<Customer> customerList = Customers.getCustomers();
		Optional<Customer> customer = customerList.stream().filter(c->c.getId()==userId).findFirst();
		if(userId!=null&&customer!=null){
			
			if(customer.get().getEmail().equals(username) && customer.get().getPassword().equals(password)){
				if(rolesSet.contains(userRole)){
					return true;
				}				
				
			}
		}
			
		isAllowed= isAdminAllowed(username, password, rolesSet);
		return isAllowed;
	}
}

