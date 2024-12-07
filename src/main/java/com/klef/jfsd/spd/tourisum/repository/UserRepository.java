package com.klef.jfsd.spd.tourisum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.klef.jfsd.spd.tourisum.model.User;
public interface UserRepository extends JpaRepository<User,Integer>{
	@Query("select a from User a where a.email=?1 and a.password=?2")
	public User checkUserLogin(String email,String password);
	@Query("select a from User a where a.email=?1 ")
	public User checkUserByEmail(String email);
    @Query("select a from User a where a.id=?1")
    public User getUserById(int uid);
    @Query("update User u set u.name=?1,u.sex=?2,u.contact=?3 where u.id=?4")
	 @Modifying 
	 @Transactional 
	 public int updateUserById(String name,String sex,long contact,int id);
}
