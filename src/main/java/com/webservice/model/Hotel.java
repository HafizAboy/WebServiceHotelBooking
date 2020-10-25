package com.webservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
@Entity
@Table(name = "t_hotels")
public class Hotel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable=false)
    @JsonIgnore
    private UUID id;

    @NotNull(message = "Hotel name cannot be null")
	@Column(name="hotel_name")
	private String hotelName;

    @Column(name="hotel_address", length=50)
    private String HotelAddress;

    @Column(name="hotel_code", unique = true, nullable=false)
    private String hotelCode;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

}
