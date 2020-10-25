package com.webservice.view;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class RoomView {

    @ApiModelProperty(notes = "Room name.")
	private String roomName;

    @ApiModelProperty(notes = "Number of guests.")
    private int capacity;

    @Length(min=1, max=1, message = "Room availability exceeded more than 1 character")
    @ApiModelProperty(notes = "Room availability.", example = "Y/N")
    private String availability;

    @ApiModelProperty(notes = "Integration with customer table.")
    private String hotelCode;

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

}
