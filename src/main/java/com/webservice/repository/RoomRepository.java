package com.webservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webservice.model.Room;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface RoomRepository extends JpaRepository<Room, UUID> {

	Room findByRoomCode(String roomCode);
	
}
