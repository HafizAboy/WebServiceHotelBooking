package com.webservice.view;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class ReservationView {

    private LocalDateTime checkInDate;

    private LocalDateTime checkOutDate;

    @ApiModelProperty(notes = "Integration with customer table.")
    private String customerCode;

    @ApiModelProperty(notes = "Integration with room table.")
    private String roomCode;

    @Length(min=1, max=1, message = "Cancellation exceeded more than 1 character")
    @ApiModelProperty(notes = "Cancellation.", example = "Y/N")
    private String cancellation;

	public LocalDateTime getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDateTime checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDateTime getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDateTime checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getCancellation() {
		return cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

}
