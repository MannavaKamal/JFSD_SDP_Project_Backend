package com.klef.jfsd.spd.tourisum.repository;



import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
@Repository
public interface HotelAdminRepository  extends JpaRepository<HotelAdmin,Integer>{
	@Query("select a from HotelAdmin a where a.email=?1 and a.password=?2")
	public HotelAdmin checkHotelAdminLogin(String email,String password);
	@Query("select a from HotelAdmin a where a.email=?1")
	public HotelAdmin checkHotelAdminByEmail(String email);

	@Query("select  a from HotelAdmin a where a.country=?1 and a.state=?2 and a.city=?3")
	public List<HotelAdmin> getAllHotels(String country,String state,String city );
	
	@Query("select a from HotelAdmin a where a.id=?1")
	public HotelAdmin getHotelAdminById(int id);
	
}
