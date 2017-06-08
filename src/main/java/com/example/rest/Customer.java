package com.example.rest;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "firstName", "lastName", "email", "city", "streetName", "streetNum", "zip"})
public class Customer {

	  private int id;
	  private String firstName;
	  private String lastName;
	  private String email;
	  private String city;
	  private String streetName;
	  private String streetNum;
	  private String zip;
	  
	  
	  public Customer(int id, String first, String last, String email, String city, String street, String streetNum, String zip){
      this.id=id;
      this.firstName=first;
      this.lastName=last;
      this.email=email;
      this.city=city;
      this.streetName=street;
      this.streetNum=streetNum;
      this.zip=zip;
      
    
				  
	  }
	  public Customer(){}

	public int getId() {		
		return this.id;
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getEmail(){
		return email;
	}
	public String getCity(){
		return city;
	}
	public String getStreetName(){
		return streetName;
	}
	public String getStreetNum(){
		return streetNum;
	}
	public String getZip(){
		return zip;
	}
	
	
	 
}