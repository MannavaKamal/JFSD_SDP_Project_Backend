package com.klef.jfsd.spd.tourisum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.spd.tourisum.model.Admin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.touristSpot;
import com.klef.jfsd.spd.tourisum.repository.AdminRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.TouristSpotRepository;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private TouristSpotRepository touristspotrepository;
	@Autowired
	private AdminRepository adminrepository;
	@Autowired 
	private RoomsRepository roomsrepository;

	@Override
	public Admin checkAdminLogin(String email, String password) {
		
		return adminrepository.checkAdminLogin(email, password);
	}

	@Override
	public void insertLocation(touristSpot tp) {
		// TODO Auto-generated method stub
		touristspotrepository.save(tp);	
	}

	@Override
	public List<touristSpot> getLocations() {
		// TODO Auto-generated method stub
		return touristspotrepository.findAll();
	}

	@Override
	public int deleteSpotById(int id) {
		// TODO Auto-generated method stub
		return touristspotrepository.deleteSpotById(id);
	}

	@Override
	public List<Rooms> getAllBookingRooms() {
		// TODO Auto-generated method stub
		System.out.println("in Server  = "+roomsrepository);
		return roomsrepository.getAllBookingRooms();
	}

//	@Override
//	public int updateDateAndUserIdAndRoomAvailable(int sno) {
//		// TODO Auto-generated method stub
//		return roomsrepository.updateDateAndUserIdAndRoomAvailable(sno); 
//	}
	
	
	

}
