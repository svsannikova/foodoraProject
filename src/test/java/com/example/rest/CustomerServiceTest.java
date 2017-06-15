package com.example.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

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
        String responseMsg = target.path("customers/all").request().get(String.class);
        ArrayList<Customer> list = mapper.readValue(responseMsg,
        		TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,  
        		   Customer.class));
        assertEquals(2, list.size());
    }
    
    @Test
    public void getCustomerByIdTest() throws JsonParseException, JsonMappingException, IOException {
        String responseMsg = target.path("customers/1").request().get(String.class);
        System.out.println(responseMsg);
        Customer list = mapper.readValue(responseMsg, Customer.class);
        assertEquals("John", list.getFirstName());
    }
    @Test
    public void createNewCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	Form form = new Form();
    	form.param("id", "6").param("firstName", "Test");
        Response responseMsg = target.path("customers").request().post(Entity.form(form));  
        System.out.println(responseMsg.toString());
        assertEquals(200, responseMsg.getStatus());
    }
    
    @Test
    public void updateCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	Form form = new Form();
    	form.param("id", "6").param("firstName", "NewName");
        Response responseMsg = target.path("customers/6").request().put(Entity.form(form));  
        ArrayList<Customer> updatedList = Customers.getCustomers();
        Optional<Customer> updatedCustomer=updatedList.stream().filter(c->c.getId()==6).findFirst();
        assertEquals("NewName", updatedCustomer.get().getFirstName());
    }
    
    @Test
    public void deleteCustomerTest() throws JsonParseException, JsonMappingException, IOException {
    	ArrayList<Customer> oldList = Customers.getCustomers();
    	System.out.println("Old size "+oldList.size());
        Response responseMsg = target.path("customers/6").request().delete(); 
        ArrayList<Customer> newList = Customers.getCustomers();
    	System.out.println("New size "+newList.size());
        assertEquals(2, newList.size());
    }
    
}
