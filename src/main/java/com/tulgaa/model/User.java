package com.tulgaa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(DataTablesOutput.View.class)
	private long id;

	@NotNull
	@Size(min = 3, max = 80)
	@JsonView(DataTablesOutput.View.class)
	private String email;
	
	@Size(min = 3, max = 80)
	@JsonView(DataTablesOutput.View.class)
	private String password;

	@NotNull
	@Size(min = 2, max = 80)
	@JsonView(DataTablesOutput.View.class)
	private String name;

	public User() { }

	public User(long id) { 
		this.id = id;
	}

	public User(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long value) {
		this.id = value;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public String getName() {
		return name;
	}
	
	public String getPassword(){
		return this.password;	
	}

	public void setName(String value) {
		this.name = value;
	}

}