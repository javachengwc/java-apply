package com.entity;

public class Outer {
	
	 private String name="ot";
	
	 public String getName() {
		return Outer.this.name;
	}

	public class Inner   
	 {   
		 private String name ="in";
		 
	     public String getSelfName()   
	     {   
	         return name;   
	     }  
	       
	     public String getOuterName()   
	     {   
	         return Outer.this.getName(); 
	     }   
	 }   
	   
	 public Inner createInner()   
	 {   
	     return new Inner();   
	 }   

}
