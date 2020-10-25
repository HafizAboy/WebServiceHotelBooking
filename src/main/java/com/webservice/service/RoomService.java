package com.webservice.service;

import java.util.List;
import java.util.UUID;

import com.webservice.model.Room;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface RoomService {

	Room findById(UUID id);
	Room findByRoomCode(String roomCode);
    List<Room> findAllRooms();
	Room saveRoom(Room room) throws Exception;
	Room updateRoom(Room room) throws Exception;
	void deleteRoom(Room room) throws Exception;
}
