package com.klef.jfsd.spd.tourisum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.klef.jfsd.spd.tourisum.model.TravelPlanRequest;

@org.springframework.stereotype.Repository
public interface TravelPlanRequestRepository extends JpaRepository<TravelPlanRequest,Integer> {
	@Query(value="select a from TravelPlanRequest a where a.userid=?1")
	  public abstract java.util.List getrequestsbyid(int id);
	
	@org.springframework.data.jpa.repository.Query(value="update TravelPlanRequest r set r.adminstatus=?1,r.amount=?2,r.tgname=?3,r.tgcontact=?4 where r.requestid=?5")
	  @org.springframework.data.jpa.repository.Modifying
	  @org.springframework.transaction.annotation.Transactional
	  public abstract int requestStatusBasedOnReqid(int adminstatus, long amount, java.lang.String name, long tgcontact, int requestid);
	
	@org.springframework.data.jpa.repository.Query(value="update TravelPlanRequest r set r.adminstatus=?1,r.reasonforreject=?2 where r.requestid=?3")
	  @org.springframework.data.jpa.repository.Modifying
	  @org.springframework.transaction.annotation.Transactional
	  public abstract int requestStatusBasedOnReqid1(int adminstatus, java.lang.String reasonforreject, int requestid);
	
	@org.springframework.data.jpa.repository.Query(value="update TravelPlanRequest r set r.paymentstatus=?1 where r.requestid=?2")
	  @org.springframework.data.jpa.repository.Modifying
	  @org.springframework.transaction.annotation.Transactional
	  public abstract int updatePaymentStatusBasedOnReqid(int paymentStatus, int requestid);
	
}
