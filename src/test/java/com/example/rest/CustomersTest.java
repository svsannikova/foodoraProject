package com.example.rest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


import com.example.rest.Customer;
import com.example.rest.Customers;
public class CustomersTest {
	Customers customerList= new Customers();
	
	@Test
	 public void addCustomerTest()  {  
		Customer customer = new Customer(6, "Name", null, "mail", "password", null, null, null, null);
		
		int result = customerList.addCustomer(customer);
       
        assertEquals(1, result);
    }
	@Test
	 public void addExistingCustomerTest()  {  
		Customer customer = new Customer(5, "Name", null, "mail", "password", null, null, null, null);
		
		int result = customerList.addCustomer(customer);
      
       assertEquals(0, result);
   }
	@Test
	 public void updateCustomerTest()  {  
		Customer customer = new Customer(1, "NewName", null, "mail", "password", null, null, null, null);
		
		int result = customerList.updateCustomer(customer);
      
       assertEquals(1, result);
   }
	@Test
	 public void updateNotExistingCustomerTest()  {  
		Customer customer = new Customer(7, "NewName", null, "mail", "password", null, null, null, null);
		
		int result = customerList.updateCustomer(customer);
     
      assertEquals(0, result);
  }
	
	@Test
	 public void deleteCustomerTest()  {  
		
		int result = customerList.deleteCustomer(5);
     
      assertEquals(1, result);
  }
	@Test
	 public void deleteNotExistingCustomerTest()  {  
		
		int result = customerList.deleteCustomer(8);
    
     assertEquals(0, result);
 }
    
	

}
