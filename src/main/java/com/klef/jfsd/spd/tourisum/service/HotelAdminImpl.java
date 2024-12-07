package com.klef.jfsd.spd.tourisum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.repository.HotelAdminRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.UserRepository;

@Service
public class HotelAdminImpl implements HotelAdminService{
	@Autowired
	private HotelAdminRepository hoteladminrepository;
	@Autowired
	private RoomsRepository roomsrepository;
	@Autowired
	private UserRepository userrepository;

	@Override
	public void inserthoteladmin(HotelAdmin h1) {
		 hoteladminrepository.save(h1);
	}

	@Override
	public HotelAdmin checkHotelAdminLogin(String email, String password) {
		return hoteladminrepository.checkHotelAdminLogin(email,password);
	}

	@Override
	public Rooms checkroom(int id, int roomno) {
		return roomsrepository.checkroom(id, roomno);
	}

	@Override
	public void hoteladminaddrooms(Rooms r1) {
		roomsrepository.save(r1);	
	}

	@Override
	public List<Rooms> getAllRoomsById(int id) {
		// TODO Auto-generated method stub
		return roomsrepository.getAllRoomsById(id);
	}


	@Override
	public int deleteRoomBySno(int sno) {
		
		return roomsrepository.deleteRoomBySno(sno);
	}

	@Override
	public Rooms getRoomBySno(int sno) {
		// TODO Auto-generated method stub
		return  roomsrepository.getRoomBySno(sno);
	}
	
	@Override
	public int updateRoomCostAndRoomTypeBySno(String roomtype, int roomcost, int sno) {
		// TODO Auto-generated method stub
		return roomsrepository.updateRoomCostAndRoomTypeBySno(roomtype,roomcost, sno);
	}

	@Override
	public List<Rooms> getLiveRoomsByHoteAdminIdAndRommAvailable(int id) {
		// TODO Auto-generated method stub
		return roomsrepository.getLiveRoomsByHoteAdminIdAndRommAvailable(id);
	}

	@Override
	public User getUserById(int id) {
		// TODO Auto-generated method stub
		return userrepository.getUserById(id);
	}
	

}
