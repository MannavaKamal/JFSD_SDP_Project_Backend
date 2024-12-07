package com.klef.jfsd.spd.tourisum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.klef.jfsd.spd.tourisum.model.Rooms;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms,Integer> {
	@Query("select a from Rooms a where a.id=?1 and a.roomno=?2")
	public Rooms checkroom(int id,int roomno);
	@Query("select a from Rooms a where a.id=?1")
    public List<Rooms> getAllRoomsById(int id);
	@Query("select a from Rooms a where a.sno=?1")
	public Rooms getHotelAdminId(int sno);
	 
	
	 
	 @Query("select a from Rooms a where a.id=?1 and a.roomavailable=false")
	 public List<Rooms> getLiveRoomsByHoteAdminIdAndRommAvailable(int haid);
	 
	 @Query("select a from Rooms a where a.roomavailable=false")
	 public List<Rooms> getAllBookingRooms();
	 
	 
	 @Query("delete from Rooms r where r.sno=?1")
	 @Modifying 
	 @Transactional 
	 public int deleteRoomBySno(int sno);
	 @Query("select r from Rooms r where r.sno=?1")
	 public Rooms getRoomBySno(int sno);
	 @Query("update Rooms r set r.roomtype=?1,r.roomcost=?2 where r.sno=?3")
	 @Modifying 
	 @Transactional 
	 public int updateRoomCostAndRoomTypeBySno(String roomtype ,int roomcost, int sno);
}
