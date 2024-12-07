package com.klef.jfsd.spd.tourisum.controller;


import java.text.ParseException;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.RoomSchedule;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.TravelPlanRequest;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.model.paymentdetails;
import com.klef.jfsd.spd.tourisum.model.touristSpot;
import com.klef.jfsd.spd.tourisum.repository.AdminRepository;
import com.klef.jfsd.spd.tourisum.repository.HotelAdminRepository;
import com.klef.jfsd.spd.tourisum.repository.PaymentRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomScheduleRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.TouristSpotRepository;
import com.klef.jfsd.spd.tourisum.repository.TravelPlanRequestRepository;
import com.klef.jfsd.spd.tourisum.repository.UserRepository;
import com.klef.jfsd.spd.tourisum.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {
	
	@Autowired
	private TravelPlanRequestRepository travelplanrequestrepository;
	@Autowired
	private AdminRepository adminrepo;
	@Autowired
	private HotelAdminRepository hoteladminrepo;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private RoomsRepository roomsrepository;
	@Autowired
	private RoomScheduleRepository roomschedulerepository;
	@Autowired
	 private JavaMailSender mailSender;
    @Autowired
    private UserService userservice;

    
    
    @GetMapping("/checkusersession")
    public User checkusersession(HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	
    	User user1 = (User)session.getAttribute("user");
    	if(user1!=null) {
    		session.setMaxInactiveInterval(3600);
    		return user1;
    	}else {
    		return null;
    	}
    }
    @GetMapping("/checkusersession1")
    public User checkusersession1(HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
    	User user1 = (User)session.getAttribute("user");
    	if(user1!=null) {
    		return user1;
    	}else {
    		return null;
    	}
    }

    @GetMapping("/checkusersignupsession")
    public User checkusersignupsession(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	User user = (User)session.getAttribute("usersignup");
    	if(user!=null) {
    		return user;
    	}else {
    		return null;
    	}
    }
    @GetMapping("/removeusersignupsession")
    public void removeusersession(HttpServletRequest request) {
    	HttpSession session = request.getSession();    	
    	session.removeAttribute("usersignup");
    }
    @PostMapping("/UserSignup")
    public String  usersignup(@RequestBody User user,HttpServletRequest request) throws Exception {
    	try {
    	if(userrepo.checkUserByEmail(user.getEmail())!=null || hoteladminrepo.checkHotelAdminByEmail(user.getEmail())!=null || adminrepo.checkAdminByEmail(user.getEmail())!=null) {
			return "2";
		}					
    	HttpSession session =  request.getSession();
    	session.removeAttribute("hoteladmindetails");
    	session.removeAttribute("pay");
    	session.removeAttribute("admindetails");
    	session.removeAttribute("user");    	
    	session.setMaxInactiveInterval(600);
    	session.setAttribute("usersignup", user);
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);        
        int otp = (int)(Math.random() * 99999); // random number generation
        session.setAttribute("otpuser", otp);
        helper.setTo(user.getEmail());
        helper.setSubject("OTP from Tourisum.jfsd.sdp.com");
        helper.setFrom("mannava.kamal@gmail.com");
        String htmlContent =         
        "<p><strong>OTP:</strong> " + otp + "</p>" +
        "<p><strong>Note:</strong>" + "this otp expires in 10 minutes"+ " </p>";
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);    	
		return "1";
		}
    	catch(Exception ex) {
    		System.out.println(ex.getMessage());
    		return ex.getMessage();
    	}
    }
    
    @PostMapping("/checkotpuser")
	public String checkotp(@RequestBody User user,HttpServletRequest request) throws Exception  {		
		if (checkusersignupsession(request)==null)
		{
			return null;
		}
		HttpSession session = request.getSession();
    	User user2 = (User)session.getAttribute("usersignup");
    	int otp = (int)session.getAttribute("otpuser");
    	if( user.getId()==otp) {
    		userservice.usersignup(user2);
    		session.removeAttribute("otpuser");
    		session.removeAttribute("usersignup");
      return "1";
    	}
    	return "0";	
	}

    @GetMapping("/UserProfile")
    public User userprofile(HttpServletRequest request) {
    	User u1 = checkusersession1(request);
    	if (u1!=null) {
    		return u1;
    	}else {
    		return null;
    	}
    }
    
    @PostMapping("postuserbyid")
	public User getuserbyid(@RequestBody User user,HttpServletRequest request) {
    	User user2 = checkusersession1(request);
		if(user2 == null ) {
			return null;
		}
		if(user.getId() != user2.getId()) {
			user.setName("");
			return user;
		}
		user = userrepo.findById(user.getId()).get();
		if(user == null) {   // if user is in session and if account deleted 		
			user2.setName(" ");
			return user2;
		}
	return  user;
	}
    @PostMapping("/userprofileupdate")
    public Integer userprofileupdate(@RequestBody User user1, HttpServletRequest request)
    {
    	User user = checkusersession1(request);
    	if(user==null) {
    		return null;
    	}
    	System.out.println("in profile update");
        User user2 = userrepo.findById(user1.getId()).get();
        if(user2 == null) {
        	return 0;
        }
        user2.setName(user1.getName());
        user2.setContact(user1.getContact());
        user2.setSex(user1.getSex());
        user2.setUserimageinbytes(user1.getUserimageinbytes());
        userrepo.save(user2);
    	return 1;
    }
   // getLoation is also a part of user
    @GetMapping("/getLocationsForUser")
	public List<touristSpot> getLocations(HttpServletRequest request){
		return userservice.getallspots();
	}
	@PostMapping("/GetAllHotels")
	public List<HotelAdmin> getallhotels(@RequestBody HotelAdmin h1){
		return userservice.getAllHotels(h1.getCountry(), h1.getState(), h1.getCity());
	}
	@PostMapping("/getRooms")
	public List<Rooms> getRooms(@RequestBody HotelAdmin ha){
		return userservice.getAllRoomsById(ha.getId());
	}
	
	@PostMapping("/pay")
	public TravelPlanRequest pay(@RequestBody TravelPlanRequest t1,HttpServletRequest request) throws RazorpayException {
	User user = checkusersession1(request);
	if(user == null){
		return null;
	}
	HttpSession session = request.getSession();
	session.setMaxInactiveInterval(3600);
		System.out.println(t1.getAmount());
		System.out.println(t1.toString());
		RazorpayClient razorpay = new RazorpayClient("rzp_test_dPcBqV32stqs3P", "dWUhjTk5EDiM0FoE1lmEce9l");
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount",t1.getAmount());
		orderRequest.put("currency","INR");
		Order order = razorpay.orders.create(orderRequest);
		//Order order = razorpay.orders.fetch("order_PJ4OYZ2q3PE00S");
		JSONObject orderResponse = order.toJson();		
		t1.setStartdate(orderResponse.getString("id")); // order id
     	session.setAttribute("pay",t1);
     TravelPlanRequest t2 = (TravelPlanRequest)session.getAttribute("pay");    
		return t2;
	}
	@GetMapping("/paymentsuccessorfailure")
	public Integer paymentsuccess(HttpServletRequest request) throws RazorpayException,ParseException
	{ 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		HttpSession session = request.getSession();
		TravelPlanRequest t1 = (TravelPlanRequest)session.getAttribute("pay");
		RazorpayClient razorpay = new RazorpayClient("rzp_test_dPcBqV32stqs3P", "dWUhjTk5EDiM0FoE1lmEce9l");
		if(t1!=null) {
		Order order = razorpay.orders.fetch(t1.getStartdate());
				JSONObject orderResponse = order.toJson();
				int status = orderResponse.getString("status").compareTo("paid");
				if(status == 0 ) {
					paymentdetails details = new paymentdetails();
					
		 LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");	        
	        details.setDate(now.format(formatter));
	        details.setOrderid(t1.getStartdate());// orderid
	        details.setUserid(user.getId());
	        userservice.addpayment(details);  
	        //userservice.updateRoomAvailableAndUserIdBySno(user.getId(),now.format(formatter), pay.getRoom_sno());	
	        travelplanrequestrepository.updatePaymentStatusBasedOnReqid(1, t1.getRequestid());
		session.removeAttribute("pay");
		return 1;
				}
					 LocalDateTime now = LocalDateTime.now();
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					paymentdetails details = new paymentdetails();
					  details.setDate(now.format(formatter));
					  details.setOrderid(t1.getStartdate());// orderid
				        details.setUserid(user.getId());
				        userservice.addpayment(details);
				        travelplanrequestrepository.updatePaymentStatusBasedOnReqid(2, t1.getRequestid());
				        session.removeAttribute("pay");
				        return 0;
		}
		
		return 2;
								
	}
	
	@PostMapping("/pay1")
	public RoomSchedule pay1(@RequestBody RoomSchedule rs,HttpServletRequest request) throws RazorpayException {
	User user = checkusersession1(request);
	if(user == null){
		return null;
	}
	HttpSession session = request.getSession();
	session.setMaxInactiveInterval(3600);
		System.out.println(rs.getOrderid());
		System.out.println(rs.toString());
		RazorpayClient razorpay = new RazorpayClient("rzp_test_dPcBqV32stqs3P", "dWUhjTk5EDiM0FoE1lmEce9l");
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount",rs.getOrderid());
		orderRequest.put("currency","INR");
		Order order = razorpay.orders.create(orderRequest);
		//Order order = razorpay.orders.fetch("order_PJ4OYZ2q3PE00S");
		JSONObject orderResponse = order.toJson();	
     	session.setAttribute("pay1",rs);
     	session.setAttribute("orderid", orderResponse.getString("id"));
     	RoomSchedule rs2 = new RoomSchedule();
		rs2.setCheckintime(orderResponse.getString("id"));
		return rs2;
	}
	
	@GetMapping("/paymentsuccessorfailure1")
	public Integer paymentsuccess1(HttpServletRequest request) throws RazorpayException,ParseException
	{ 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		HttpSession session = request.getSession();
		RoomSchedule rs = (RoomSchedule)session.getAttribute("pay1");
		rs.setOrderid(0);
		rs.setUserid(user.getId());
		
		String orderid = (String)session.getAttribute("orderid");
		RazorpayClient razorpay = new RazorpayClient("rzp_test_dPcBqV32stqs3P", "dWUhjTk5EDiM0FoE1lmEce9l");
		Order order = razorpay.orders.fetch(orderid);
				JSONObject orderResponse = order.toJson();
				int status = orderResponse.getString("status").compareTo("paid");
				if(status == 0 ) {
					paymentdetails details = new paymentdetails();					
		 LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");	        
	        details.setDate(now.format(formatter));
	        rs.setDate(now.format(formatter));
	        details.setOrderid(orderid);// orderid
	        details.setUserid(user.getId());
	        userservice.addpayment(details);
	        System.out.println(rs.toString());
	        roomschedulerepository.save(rs);
		session.removeAttribute("pay1");
		return 1;
				}
					 LocalDateTime now = LocalDateTime.now();
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
					paymentdetails details = new paymentdetails();
					  details.setDate(now.format(formatter));
					  details.setOrderid(orderid);// orderid
				        details.setUserid(user.getId());
				        userservice.addpayment(details);
				        session.removeAttribute("pay1");
				        return 0;
								
	}
	
	@GetMapping("/paymentdetails")
	public List<Pay> paymentdetails(HttpServletRequest request) throws RazorpayException, ParseException 
	{
		User user1 = checkusersession1(request);
		if(user1 == null) {
			return null;
		}
		List<paymentdetails> l1 = userservice.getPaymentDetailsBasedOnUserId(user1.getId());
		List<Pay> lp = new ArrayList<>();
		RazorpayClient razorpay = new RazorpayClient("rzp_test_dPcBqV32stqs3P", "dWUhjTk5EDiM0FoE1lmEce9l");
		for(paymentdetails p : l1) {
			Order order = razorpay.orders.fetch(p.getOrderid());
			JSONObject orderResponse = order.toJson();
			Pay pay1 = new Pay();
			pay1.setAmount(orderResponse.getLong("amount"));
			//pay1.setAmountDue(orderResponse.getLong("amount_due"));
			//pay1.setAmountPaid( orderResponse.getLong("amount_paid"));
			pay1.setOrderid(p.getOrderid());
			pay1.setStatus(orderResponse.getString("status"));
			pay1.setAttempts(orderResponse.getLong("attempts"));
//			Date date = new Date(orderResponse.getLong("created_at") * 1000L); 
//			SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//	        inputFormat.setTimeZone(TimeZone.getTimeZone("IST")); 
//	        Date date1 = inputFormat.parse(date.toString());
//	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");    
			pay1.setCreatedAt(p.getDate());
			
			lp.add(pay1);			
		}
		return lp;
				
	}
	

	
	@PostMapping("/getrequest")
	public Integer getrequest(@RequestBody TravelPlanRequest r1 ,HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		r1.setUserid(user.getId());
		travelplanrequestrepository.save(r1);
		return 1;// request created
	}
	@GetMapping("/myrequests")
	public List<TravelPlanRequest> myrequests(HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		return travelplanrequestrepository.getrequestsbyid(user.getId());
		//return null;
	}
	@PostMapping("/paymentreject")
	public Integer paymentreject(@RequestBody TravelPlanRequest t1, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		return (Integer)travelplanrequestrepository.updatePaymentStatusBasedOnReqid(3,t1.getRequestid());
		//return null;
	}
	@PostMapping("/travelplancancel")
	public String travelplancancel(@RequestBody TravelPlanRequest t1, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}
		travelplanrequestrepository.deleteById(t1.getRequestid());
		return "1";
		//return null;
	}
	
	@PostMapping("/roomidbasedonhoteladminid")
	public List<RoomSchedule> roomidbasedonhoteladminid(@RequestBody RoomSchedule rs, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}		
		return roomschedulerepository.roomidbasedonhoteladminid(rs.getHoteladminid());
	}
	@GetMapping("/roombookingsbasedonuserid")
	public List<RoomSchedule> roomidbasedonuserid( HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}		
		return roomschedulerepository.roomidbasedonuserid(user.getId());
	}
	@PostMapping("/gethoteladminbyid")
	public HotelAdmin gethoteladminbyid(@RequestBody HotelAdmin ha, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}	
	Optional<HotelAdmin> ha2 =	hoteladminrepo.findById(ha.getId());
		return ha2.get();
	}
	
	@PostMapping("/getroombyid")
	public Rooms getroombyid(@RequestBody Rooms r1, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}	
	Optional<Rooms> r2 =	roomsrepository.findById(r1.getSno());
		return r2.get();
	}
	@PostMapping("/cancelroomschedule")
	public String cancelroomschedule(@RequestBody RoomSchedule rs1, HttpServletRequest request) { 
		User user = checkusersession1(request);
		if(user == null){
			return null;
		}	
      roomschedulerepository.deleteById(rs1.getOrderid());
		return "1";
	}
	
	
	@GetMapping("/logout")// removing the session attribute
	public void logout(HttpServletRequest request) {
		
		if (checkusersession1(request)!=null) {
			HttpSession session = request.getSession();
			session.removeAttribute("user");
		}
		
	}
	
	// in session the session is unique for browser 
	@PostMapping("/dateandtime")
	public Pay dateandtime(@RequestBody Pay datetime) {
		System.out.println(datetime.getSno()+" "+datetime.getUserid()+" "+datetime.getRoom_sno()+" "+datetime.getRoomno()+" "+datetime.getAmount());
		LocalDateTime customDateTime = LocalDateTime.of(datetime.getSno(), datetime.getUserid(), datetime.getRoom_sno(),datetime.getRoomno(),(int) datetime.getAmount());
		  System.out.println("Updated Date and Time (+5 hours): " + customDateTime.toString());
      LocalDateTime updatedDateTime = customDateTime.plusHours(datetime.getAmountPaid());
      System.out.println("Updated Date and Time (+5 hours): " + updatedDateTime.toString());
      datetime.setOrderid(customDateTime.toString());
      datetime.setStatus(updatedDateTime.toString());
      return datetime;
	}
}


// to take amount from frontend
class Pay{
	 private int sno;
	 private int userid;
	 private String orderid; 
	 private int room_sno;
	 private long amount;
	 private long amountPaid; 
	 private long amountDue ;
	 private String status ;
	 private long attempts ;
	 private String createdAt;
	 private int roomno;
	 private String hotelname;
	 private String hoteladdress;
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getRoom_sno() {
		return room_sno;
	}
	public void setRoom_sno(int room_sno) {
		this.room_sno = room_sno;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(long amountPaid) {
		this.amountPaid = amountPaid;
	}
	public long getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(long amountDue) {
		this.amountDue = amountDue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getAttempts() {
		return attempts;
	}
	public void setAttempts(long attempts) {
		this.attempts = attempts;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getRoomno() {
		return roomno;
	}
	public void setRoomno(int roomno) {
		this.roomno = roomno;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getHoteladdress() {
		return hoteladdress;
	}
	public void setHoteladdress(String hoteladdress) {
		this.hoteladdress = hoteladdress;
	}
	@Override
	public String toString() {
		return "paymentdetails [sno=" + sno + ", userid=" + userid + ", orderid=" + orderid + ", room_sno=" + room_sno
				+ ", amount=" + amount + ", amountPaid=" + amountPaid + ", amountDue=" + amountDue + ", status="
				+ status + ", attempts=" + attempts + ", createdAt=" + createdAt + ", roomno=" + roomno + ", hotelname="
				+ hotelname + ", hoteladdress=" + hoteladdress + "]";
	}
}
