package com.klef.jfsd.spd.tourisum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.model.paymentdetails;
import com.klef.jfsd.spd.tourisum.model.touristSpot;
import com.klef.jfsd.spd.tourisum.repository.HotelAdminRepository;
import com.klef.jfsd.spd.tourisum.repository.PaymentRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.TouristSpotRepository;
import com.klef.jfsd.spd.tourisum.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	@Autowired	
	private HotelAdminRepository hoteladminrepository;
	@Autowired
	private RoomsRepository roomsrepository;  
	@Autowired
	private UserRepository userrepository; 
	@Autowired
	private PaymentRepository paymentrepository; 
	@Autowired 
	private TouristSpotRepository touristspotrepository;
	@Override
	public void usersignup(User user) {
		// TODO Auto-generated method stub
		userrepository.save(user);
	}

	@Override
	public User checkUserLogin(String email, String password) {
		// TODO Auto-generated method stub
		return  userrepository.checkUserLogin(email, password);
	}

	@Override
	public int updateUserById(String name, String sex, long contact, int id) {
		
		return userrepository.updateUserById(name, sex, contact, id);
	}

	@Override
	public List<HotelAdmin> getAllHotels(String country, String state, String city) {
		// TODO Auto-generated method stub
		return hoteladminrepository.getAllHotels(country, state, city);
	}

	@Override
	public List<Rooms> getAllRoomsById(int id) {
		// TODO Auto-generated method stub
		return roomsrepository.getAllRoomsById(id);
	}

	@Override
	public void addpayment(paymentdetails details) {
		// TODO Auto-generated method stub
		paymentrepository.save(details);  
	}
	
//	@Override
//	public int updateRoomAvailableAndUserIdBySno(int userid, String date, int sno) {
//		// TODO Auto-generated method stub
//		return roomsrepository.updateRoomAvailableAndUserIdBySno(userid,date,sno);		
//	}

	@Override
	public List<paymentdetails> getPaymentDetailsBasedOnUserId(int userid) {
		// TODO Auto-generated method stub
		return paymentrepository.getPaymentDetailsBasedOnUserId(userid);
	}

//	@Override
//	public List<Rooms> getLiveRoomsById(int userid) {
//		// TODO Auto-generated method stub
//		return roomsrepository.getLiveRoomsById(userid);
//	}

	@Override
	public HotelAdmin getHotelAdminById(int id) {
		// TODO Auto-generated method stub
		return hoteladminrepository.getHotelAdminById(id);
	}

	@Override
	public List<touristSpot> getallspots() {
		return touristspotrepository.findAll();
	}

	
	

}
