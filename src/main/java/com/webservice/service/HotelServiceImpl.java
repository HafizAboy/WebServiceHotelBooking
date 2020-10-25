package com.webservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.model.Hotel;
import com.webservice.repository.HotelRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
	private HotelRepository hotelRepo;

	@Override
	public Hotel findById(UUID id) {
		// TODO Auto-generated method stub
		return hotelRepo.getOne(id);
	}
	
	@Override
	public List<Hotel> findAllHotels() {
		// TODO Auto-generated method stub
		return (List<Hotel>)hotelRepo.findAll();
	}

	@Override
	public Hotel saveHotel(Hotel hotel) throws Exception {
		// TODO Auto-generated method stub
		return hotelRepo.save(hotel);
	}

	@Override
	public Hotel updateHotel(Hotel hotel) throws Exception {
		// TODO Auto-generated method stub
		return saveHotel(hotel);
	}

	@Override
	public void deleteHotel(Hotel hotel) throws Exception {
		// TODO Auto-generated method stub
		hotelRepo.delete(hotel);
	}

	@Override
	public Hotel findByHotelCode(String hotelCode) {
		// TODO Auto-generated method stub
		return hotelRepo.findByHotelCode(hotelCode);
	}

}
