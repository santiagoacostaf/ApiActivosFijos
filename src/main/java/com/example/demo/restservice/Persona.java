package com.example.demo.restservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity 
public class Persona {
	 @Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
	  private Integer id;
	 String name;
	 
	 public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	 public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

}
