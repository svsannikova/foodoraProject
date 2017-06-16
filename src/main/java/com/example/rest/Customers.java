package com.example.rest;

import java.util.ArrayList;
import javax.ws.rs.Path;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class Customers {
	private static ArrayList<Customer> customerList = new ArrayList<Customer>();
	
	static{
		Customer customer = new Customer(1, "John", "Smith", "john.smith@gmail.com","password", "Frankfurt", "Zeil", "1B", "65454" );
		Customer customer2 = new Customer(5, "Another", "Example", "a.example@gmail.com","password", "Helsinki", "Aleksanterinkatu", "2", "00100" );
		customerList.add(customer);
		customerList.add(customer2);
	}
	public Customers(){};
	
	public static ArrayList<Customer> getCustomers(){
		return customerList;
	}
	
	public int addCustomer(Customer nCustomer){ 
	      ArrayList<Customer> customerList = getCustomers(); 
	      boolean Exists = false; 
	      for(Customer customer: customerList){ 
	         if(customer.getId() == nCustomer.getId()){ 
	            Exists = true; 
	            break; 
	         } 
	      }   
	      if(!Exists){ 
	         customerList.add(nCustomer); 
	         return 1; 
	      } 
	      return 0; 
	   } 
	
	public int updateCustomer(Customer nCustomer){ 
	      ArrayList<Customer> customerList = getCustomers();  
	      for(Customer customer: customerList){ 
	         if(customer.getId() == nCustomer.getId()){ 
	        	 int index = customerList.indexOf(customer);    
	             customerList.set(index, nCustomer); 
	             return 1;	             
	         } 
	      } 
	      return 0; 
	   } 
	
	public int deleteCustomer(long id){ 
	      ArrayList<Customer> customerList = getCustomers();  
	      for(Customer customer: customerList){ 
	         if(customer.getId() == id){ 
	        	 int index = customerList.indexOf(customer);    
	             customerList.remove(index); 
	             return 1;	             
	         } 
	      } 
	      return 0; 
	   } 
}
