package com.webservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservice.model.Room;
import com.webservice.repository.RoomRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
	private RoomRepository roomRepo;

	@Override
	public Room findById(UUID id) {
		// TODO Auto-generated method stub
		return roomRepo.getOne(id);
	}
	
	@Override
	public List<Room> findAllRooms() {
		// TODO Auto-generated method stub
		return (List<Room>)roomRepo.findAll();
	}

	@Override
	public Room saveRoom(Room room) throws Exception {
		// TODO Auto-generated method stub
		return roomRepo.save(room);
	}

	@Override
	public Room updateRoom(Room room) throws Exception {
		// TODO Auto-generated method stub
		return saveRoom(room);
	}

	@Override
	public void deleteRoom(Room room) throws Exception {
		// TODO Auto-generated method stub
		roomRepo.delete(room);
	}

	@Override
	public Room findByRoomCode(String roomCode) {
		// TODO Auto-generated method stub
		return roomRepo.findByRoomCode(roomCode);
	}

}
