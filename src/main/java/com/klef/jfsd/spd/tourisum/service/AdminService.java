package com.klef.jfsd.spd.tourisum.service;

import java.util.List;

import com.klef.jfsd.spd.tourisum.model.Admin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.touristSpot;

public interface AdminService {
	public Admin checkAdminLogin(String email,String password);
	public void insertLocation(touristSpot tp);
	public List<touristSpot> getLocations();
	public int deleteSpotById(int id);
	 public List<Rooms> getAllBookingRooms();
	// public int updateDateAndUserIdAndRoomAvailable(int sno);

}
