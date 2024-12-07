package com.klef.jfsd.spd.tourisum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.spd.tourisum.model.Admin;

@Repository
public interface AdminRepository  extends JpaRepository<Admin,Integer>{
	
	@Query("select a from Admin a where a.email=?1 and a.password=?2")
	public Admin checkAdminLogin(String email,String password);
	@Query("select a from Admin a where a.email=?1")
	public Admin checkAdminByEmail(String email);
	

}
