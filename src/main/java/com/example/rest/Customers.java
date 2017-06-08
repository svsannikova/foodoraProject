package com.example.rest;


import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class Customers {
	private static ArrayList<Customer> customerList = new ArrayList<Customer>();
	
	static{
		Customer customer = new Customer(1, "John", "Smith", "john.smith@gmail.com", "Frankfurt", "Zeil", "1B", "65454" );
		Customer customer2 = new Customer(5, "Another", "Example", "a.example@gmail.com", "Helsinki", "Aleksanterinkatu", "2", "00100" );
		customerList.add(customer);
		customerList.add(customer2);
	}
	private Customers(){};
	
	public static ArrayList<Customer> getCustomers(){
		return customerList;
	}
	
}
