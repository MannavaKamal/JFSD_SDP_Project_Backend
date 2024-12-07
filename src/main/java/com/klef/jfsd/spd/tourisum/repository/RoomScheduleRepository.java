package com.klef.jfsd.spd.tourisum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.spd.tourisum.model.RoomSchedule;

@Repository
public interface RoomScheduleRepository  extends JpaRepository<RoomSchedule,Integer>{
	
	@Query("select a from RoomSchedule a where a.hoteladminid=?1")
	public List<RoomSchedule> roomidbasedonhoteladminid(int hoteladminid);
	@Query("select a from RoomSchedule a where a.userid=?1")
	public List<RoomSchedule> roomidbasedonuserid(int userid);
}
