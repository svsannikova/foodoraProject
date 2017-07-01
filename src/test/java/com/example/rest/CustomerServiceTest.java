package com.example.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import javax.xml.bind.DatatypeConverter;


import com.example.rest.Customer;

public class CustomerServiceTest {

    private HttpServer server;
    private WebTarget target;
    ObjectMapper mapper = new ObjectMapper();
    

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
        
    }

    @SuppressWarnings("deprecation")
	@After
    public void tearDown() throws Exception {
        server.stop();
    }

    
    
    @Test
    public void getAllCustomersTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="admin:admin";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
        String responseMsg = target.path("customers/all").request().header(HttpHeaders.AUTHORIZATION, key).get(String.class);
        ArrayList<Customer> list = mapper.readValue(responseMsg,
        		TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,  
        		   Customer.class));
        assertEquals(2, list.size());
    }
    
    @Test
    public void getCustomerByIdTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="admin:admin";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
        String responseMsg = target.path("customers/1").request().header(HttpHeaders.AUTHORIZATION, key).get(String.class);
        Customer list = mapper.readValue(responseMsg, Customer.class);
        assertEquals("John", list.getFirstName());
    }
    @Test
    public void createNewCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	String jsonString = "{\"id\": 6,\"firstName\": \"Name\", \"email\": \"mail\", \"password\": \"password\"}";    	
        Response responseMsg = target.path("customers").request().post(Entity.json(jsonString));  
        System.out.println(responseMsg.toString());
        assertEquals(200, responseMsg.getStatus());
    }
    
    @Test
    public void updateCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="admin:admin";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
    	String jsonString = "{\"id\": 6,\"firstName\": \"NewName\", \"email\": \"mail\", \"password\": \"password\"}";
        Response responseMsg = target.path("customers/6").request().header(HttpHeaders.AUTHORIZATION, key).put(Entity.json(jsonString));  
        ArrayList<Customer> updatedList = Customers.getCustomers();
        Optional<Customer> updatedCustomer=updatedList.stream().filter(c->c.getId()==6).findFirst();
        assertEquals("NewName", updatedCustomer.get().getFirstName());
    }
    
    @Test
    public void deleteCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="admin:admin";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
        Response responseMsg = target.path("customers/6").request().header(HttpHeaders.AUTHORIZATION, key).delete(); 
        ArrayList<Customer> newList = Customers.getCustomers();
        assertEquals(2, newList.size());
    }
    @Test
    public void getAllCustomersNotAuthorizedTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="admin:noadmin";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
    	Response responseMsg = target.path("customers/all").request().header(HttpHeaders.AUTHORIZATION, key).get(Response.class);
        assertEquals(401, responseMsg.getStatus());
    }
    @Test
    public void getCustomerByIdAsCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="a.example@gmail.com:password";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
        String responseMsg = target.path("customers/5").request().header(HttpHeaders.AUTHORIZATION, key).get(String.class);
        Customer list = mapper.readValue(responseMsg, Customer.class);
        assertEquals("Another", list.getFirstName());
    }
    @Test
    public void getOtherCustomerByIdAsCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	String pass="a.example@gmail.com:password";
    	String key= "Basic " + DatatypeConverter.printBase64Binary(pass.getBytes("UTF-8"));
        Response responseMsg = target.path("customers/1").request().header(HttpHeaders.AUTHORIZATION, key).get(Response.class);
        System.out.println(responseMsg);
        assertEquals(401, responseMsg.getStatus());
    }
  
    
    
}
