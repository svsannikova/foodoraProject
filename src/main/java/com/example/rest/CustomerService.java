package com.example.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/customers")
public class CustomerService {


private ArrayList<Customer> cList = Customers.getCustomers();
ObjectMapper mapper = new ObjectMapper();
  
  @GET
  @Path("/all")
  @Produces({"application/json"})
  public String getAllCustomers() throws JsonGenerationException, JsonMappingException, IOException {
	  String outString=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cList);
    return outString;
  }

  @GET
  @Path("{id}")
  @Produces({"application/json"})
  public String getCustomer(@PathParam("id") long id) throws JsonGenerationException, JsonMappingException, IOException {
    Optional<Customer> match = cList.stream().filter(c -> c.getId() == id).findFirst();
    if (match!=null) {
    	Customer customer = match.get();
    	String outString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customer);
      return "---Customer---\n" + outString;
    } else {
      return "Customer not found";
    }
  }
 
}