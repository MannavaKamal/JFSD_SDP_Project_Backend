package com.klef.jfsd.spd.tourisum.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.klef.jfsd.spd.tourisum.model.touristSpot;
@Repository 
public interface TouristSpotRepository extends JpaRepository<touristSpot,Integer>{
@Query("select a from touristSpot a")
public List<touristSpot> getallspots();

@Query("delete from touristSpot r where r.id=?1")
@Modifying 
@Transactional 
public int deleteSpotById(int id);
}
