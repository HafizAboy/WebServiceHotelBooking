package com.webservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
@Entity
@Table(name = "t_customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable=false)
    @JsonIgnore
    private UUID id;

    @NotNull(message = "First name cannot be null")
	@Column(name="first_name")
	private String firstName;
	
    @NotNull(message = "Last name cannot be null")
	@Column(name="last_name")
	private String lastName;

    @NotNull(message = "Email cannot be null")
	@Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = "Phone Number cannot be null")
    @Column(name = "phone_number")
    private String phone;

    @Column(name="customer_code", unique = true, nullable=false)
    private String customerCode;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

}
