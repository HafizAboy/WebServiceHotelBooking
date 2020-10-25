package com.webservice.view;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import io.swagger.annotations.ApiModelProperty;

public class CustomerView {

    @ApiModelProperty(notes = "First name.")
    @NotNull(message = "First name cannot be null")
	@Column(name="first_name")
	private String firstName;

    @ApiModelProperty(notes = "Last name.")
    @NotNull(message = "Last name cannot be null")
	@Column(name="last_name")
	private String lastName;

    @ApiModelProperty(notes = "Email.")
    @NotNull(message = "Email cannot be null")
	@Email(message = "Email should be valid")
    private String email;

    @ApiModelProperty(notes = "Phone number.")
    @NotNull(message = "Phone Number cannot be null")
    private String phone;

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

}
