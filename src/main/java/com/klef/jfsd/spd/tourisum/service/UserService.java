package com.klef.jfsd.spd.tourisum.service;

import java.util.List;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.model.paymentdetails;
import com.klef.jfsd.spd.tourisum.model.touristSpot;

public interface UserService {
	
	public void usersignup(User user);
	public User checkUserLogin(String email,String password);
	 public int updateUserById(String name,String sex,long contact,int id);
	 public List<HotelAdmin> getAllHotels(String country,String state,String city );
	 public List<Rooms> getAllRoomsById(int id);
	 public void addpayment(paymentdetails details);
	// public int updateRoomAvailableAndUserIdBySno(int userid,String date,int sno);
	 public List<paymentdetails> getPaymentDetailsBasedOnUserId(int userid);
	// public List<Rooms> getLiveRoomsById(int userid);
	 public HotelAdmin getHotelAdminById(int id);
	 public List<touristSpot> getallspots();
}
