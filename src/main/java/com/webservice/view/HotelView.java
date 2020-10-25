package com.webservice.view;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class HotelView {

    @NotNull(message = "Hotel name cannot be null")
    @ApiModelProperty(notes = "Hotel name.")
	private String hotelName;

    @ApiModelProperty(notes = "Hotel address.")
    private String HotelAddress;

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelAddress() {
		return HotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		HotelAddress = hotelAddress;
	}

}
