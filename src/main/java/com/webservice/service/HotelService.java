package com.webservice.service;

import java.util.List;
import java.util.UUID;

import com.webservice.model.Hotel;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface HotelService {

	Hotel findById(UUID id);
	Hotel findByHotelCode(String hotelName);
    List<Hotel> findAllHotels();
	Hotel saveHotel(Hotel hotel) throws Exception;
	Hotel updateHotel(Hotel hotel) throws Exception;
	void deleteHotel(Hotel hotel) throws Exception;
}
