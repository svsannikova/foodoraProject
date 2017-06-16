package com.example.rest;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


@Path("/customers")
public class CustomerService {
Customers customers = new Customers();

private ArrayList<Customer> cList = Customers.getCustomers();
ObjectMapper mapper = new ObjectMapper();

  @RolesAllowed("ADMIN")
  @GET
  @Path("/all")
  @Produces({"application/json"})
  public String getAllCustomers() throws JsonGenerationException, JsonMappingException, IOException {
	  String outString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cList);
    return outString;
  }

  @RolesAllowed({"ADMIN", "AUTHORIZED_USER"})
  @GET
  @Path("{id}")
  @Produces({"application/json"})
  public String getCustomer(@PathParam("id") long id) throws JsonGenerationException, JsonMappingException, IOException {
    Optional<Customer> match = cList.stream().filter(c -> c.getId() == id).findFirst();
    if (match!=null) {
    	Customer customer = match.get();
    	String outString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customer);
      return outString;
    } else {
      return "Customer not found";
    }
  }
  
  @POST
  @Path("") 
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
  public String createCustomer(@FormParam("id") int id, 
     @FormParam("firstName") String first, 
     @FormParam("lastName") String last,
     @FormParam("email") String mail,
     @FormParam("password") String password,
     @FormParam("city") String city,
     @FormParam("streetName") String street,
     @FormParam("streetNum") String streetNum,
     @FormParam("zip") String zip){
	 Customer customer = new Customer(id, first, last, mail,password, city, street, streetNum, zip);
	 int status = customers.addCustomer(customer);
	 if(status == 1){
		 return "created";
	 }
	 return "fail";  
  } 
  @RolesAllowed({"ADMIN", "AUTHORIZED_USER"})
  @PUT
  @Path("{id}") 
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
  public String updateCustomer(@FormParam("id") int id, 
     @FormParam("firstName") String first, 
     @FormParam("lastName") String last,
     @FormParam("email") String mail,
     @FormParam("password") String password,
     @FormParam("city") String city,
     @FormParam("streetName") String street,
     @FormParam("streetNum") String streetNum,
     @FormParam("zip") String zip){
	 Customer customer = new Customer(id, first, last, mail, password, city, street, streetNum, zip);
	 int status = customers.updateCustomer(customer);
	 if(status == 1){
		 return "updated";
	 }
	 return "fail"; 
     
  } 
  @RolesAllowed({"ADMIN", "AUTHORIZED_USER"})
  @DELETE
  @Path("{id}")  
  @Produces({"application/json"})
  public String deleteCustomer(@PathParam("id") long id) throws JsonGenerationException, JsonMappingException, IOException {	 
	 int status = customers.deleteCustomer(id);
	 if(status == 1){
		 return "deleted";
	 }
	 return "fail"; 
     
  } 
  
 
}