package com.klef.jfsd.spd.tourisum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klef.jfsd.spd.tourisum.model.paymentdetails;
public interface PaymentRepository extends JpaRepository<paymentdetails,Integer>
{
	@Query("select a from paymentdetails a where a.userid=?1")
	public List<paymentdetails> getPaymentDetailsBasedOnUserId(int userid);
	
	@Query("select a from paymentdetails a where a.orderid=?1")
	public paymentdetails getPaymentDetailsBasedOnOrderId(String orderid);
	
	
  
}
