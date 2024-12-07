package com.klef.jfsd.spd.tourisum.service;

import java.util.List;



import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.User;



public interface HotelAdminService {	
  public void inserthoteladmin(HotelAdmin h1);
  public HotelAdmin checkHotelAdminLogin(String email,String password);
  public Rooms checkroom(int id,int roomno);
  public void hoteladminaddrooms(Rooms r1);
  public List<Rooms> getAllRoomsById(int id);
  public Rooms getRoomBySno(int sno);
  public int deleteRoomBySno(int sno);
  public int updateRoomCostAndRoomTypeBySno(String roomtype ,int roomcost, int sno);
  public List<Rooms> getLiveRoomsByHoteAdminIdAndRommAvailable(int id);
  public User getUserById(int id);
}
