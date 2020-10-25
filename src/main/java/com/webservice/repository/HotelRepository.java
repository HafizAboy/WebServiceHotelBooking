package com.webservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.model.Hotel;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

	Hotel findByHotelCode(String hotelCode);
}
