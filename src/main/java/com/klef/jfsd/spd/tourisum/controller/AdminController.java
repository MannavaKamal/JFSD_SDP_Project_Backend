package com.klef.jfsd.spd.tourisum.controller;


import java.time.LocalDateTime;



import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.spd.tourisum.model.Admin;
import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.RoomSchedule;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.TravelPlanRequest;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.model.paymentdetails;
import com.klef.jfsd.spd.tourisum.model.touristSpot;
import com.klef.jfsd.spd.tourisum.repository.HotelAdminRepository;
import com.klef.jfsd.spd.tourisum.repository.PaymentRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomScheduleRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.TravelPlanRequestRepository;
import com.klef.jfsd.spd.tourisum.repository.UserRepository;
import com.klef.jfsd.spd.tourisum.service.AdminService;
import com.klef.jfsd.spd.tourisum.service.HotelAdminService;
import com.klef.jfsd.spd.tourisum.service.UserService;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@RestController
public class AdminController {
	
   
	@Autowired
	private AdminService adminservice;
	@Autowired
	private TravelPlanRequestRepository travelplanrequestrepository;
	@Autowired
	private HotelAdminService hoteladminservice;
	 @Autowired
	 private UserService userservice;
	 @Autowired
    private UserRepository userrepository; 
	 @Autowired
		private RoomScheduleRepository roomschedulerepository;
	 @Autowired
		private HotelAdminRepository hoteladminrepository;
	 @Autowired
		private RoomsRepository roomsrepository;
	 @Autowired
		private PaymentRepository paymentrepository;
	 @Autowired
	 private JavaMailSender mailSender;
	 
		@PostMapping("/login")
		public int adminlogin(@RequestBody Admin a1,HttpServletRequest request ) {
			 HttpSession session = request.getSession();
			 User user1 = userservice.checkUserLogin(a1.getEmail(), a1.getPassword());
				if(user1 != null) {
					session.removeAttribute("hoteladmindetails");
					session.removeAttribute("admindetails");
		        	session.setAttribute("user",user1);
		        	session.setMaxInactiveInterval(3600);
		    		return 2;
		    	}
				 HotelAdmin ha =  hoteladminservice.checkHotelAdminLogin(a1.getEmail(), a1.getPassword());	
				 if(ha!=null) {
					 if(ha.getStatus() == 0) {
					 return 12;   // waiting for admin aprovel
					 }
				 }
					if(ha != null){
						session.removeAttribute("admindetails");
						session.removeAttribute("user");
						session.setAttribute("hoteladmindetails", ha);
						session.setMaxInactiveInterval(3600);
						return 1;
					}
			 Admin admin = adminservice.checkAdminLogin(a1.getEmail(), a1.getPassword());
			 if(admin != null){
					session.removeAttribute("user");
					session.removeAttribute("hoteladmindetails");
					session.setAttribute("admindetails", admin);
				System.out.println("session at admin login"+session.getId());
				 System.out.println(a1.toString());
					session.setMaxInactiveInterval(3600);
					return 0;
				}
				return 3;
		}	 
	 
	@GetMapping("/checkadminsession")
    public Admin checkadminsession(HttpServletRequest request) {
	    System.out.println("session at checkadmin login"+session.getId());
    	HttpSession session = request.getSession();
    	Admin a1 = (Admin)session.getAttribute("admindetails");
    	if(a1!=null) {
    		session.setMaxInactiveInterval(3600);
    		return a1;
    	}else {
    		return null;
    	}
    }
	@GetMapping("/checkadminsession1")
    public Admin checkadminsession1(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	Admin a1 = (Admin)session.getAttribute("admindetails");
    	if(a1!=null) {
    		return a1;
    	}else {
    		return null;
    	}
    }
	
	 
	@PostMapping("/insertLocation")
	public Integer insertLocation(@RequestBody touristSpot tp ,HttpServletRequest request) {
		if(checkadminsession1(request) == null ) {
			return null;
		}
		adminservice.insertLocation(tp);		
		return 1;
	}
	
	@GetMapping("/getLocations")
	public List<touristSpot> getLocations(HttpServletRequest request){
		if(checkadminsession1(request) == null ) {
			return null;
		}
		return adminservice.getLocations();
	}
	
	@PostMapping("/deletespot")
	public Integer deletespot(@RequestBody touristSpot spot,HttpServletRequest request) {
		if(checkadminsession1(request) == null ) {
			return null;
		}
		return (Integer)adminservice.deleteSpotById(spot.getId());
		//
	}
	
	@GetMapping("/adminrequests")
	public List<TravelPlanRequest> myrequests(HttpServletRequest request) { 
		if(checkadminsession1(request) == null ) {
			return null;
		}
		return travelplanrequestrepository.findAll();
		//return null;
	}
	@PostMapping("/responsefromadmin")
	public String responcefromadmin(@RequestBody TravelPlanRequest t1,  HttpServletRequest request) { 
		
		if(checkadminsession1(request) == null ) {
			return null;
		}
		System.out.println(t1.toString());	
		if(t1.getAdminstatus() == 1) {
		travelplanrequestrepository.requestStatusBasedOnReqid(t1.getAdminstatus(),t1.getAmount(),t1.getTgname(),t1.getTgcontact(),t1.getRequestid());
		}
		else {
			travelplanrequestrepository.requestStatusBasedOnReqid1(t1.getAdminstatus(),t1.getReasonforreject(),t1.getRequestid());
		}
		return null;
	}
	
	@PostMapping("getuserbyid")
	public User getuserbyid(@RequestBody User user,HttpServletRequest request) {
		if(checkadminsession1(request) == null ) {
			return null;
		}
	return userrepository.getUserById(user.getId());	
	}
	
	@PostMapping("/hoteladminapprovel")
	public Integer hoteladminapprovel(@RequestBody HotelAdmin ha, HttpServletRequest request) throws Exception{
		if(checkadminsession1(request) == null ) {
			return null;
		}
		ha.setStatus(1);
		hoteladminrepository.save(ha);
		String subject = "Notification: Account Approved for Tourisum.jfsd.sdp.com";
		String htmlcontent ="<p><strong>Admin Approved your Account you can login now with your details</strong> </p>" +
				"<p><strong>Use below details  for further communication</strong> </p>" +
		        "<p><strong>Email:</strong>" + "2200032973@kluniversity.in"+ " </p>"+
		        "<p><strong>Telegram id:</strong>" + "@Mannava_Kamal"+ " </p>"+
		        "<p><strong>Thankyou</strong></p>";		   
		sendmail(ha.getEmail(), subject, htmlcontent);
		return 1;
	}
	
	@GetMapping("/getallusers")
	public List<User> getallusers(HttpServletRequest request){
		if(checkadminsession1(request) == null ) {
			return null;
		}
		return userrepository.findAll();
	}
	@PostMapping("/deleteuserbyid")
	public Integer deleteuserbyid(@RequestBody User user,HttpServletRequest request)throws Exception {
		if(checkadminsession1(request) == null ) {
			return null;
		}
		User user2 = userrepository.findById(user.getId()).get();
		if(user2 == null) {
			return 0; // account not available 
		}
		List<RoomSchedule> lrs = roomschedulerepository.findAll();
		for(RoomSchedule rs : lrs) {
			if(rs.getUserid() == user.getId()) {
				roomschedulerepository.deleteById(rs.getOrderid());
			}
		}
		List<TravelPlanRequest> ltr = travelplanrequestrepository.findAll();
		for(TravelPlanRequest tr : ltr) {
			if(tr.getUserid() == user.getId()) {
				travelplanrequestrepository.deleteById(tr.getRequestid());
			}
		}
		List<paymentdetails> lpd = paymentrepository.findAll();
		for(paymentdetails pd : lpd) {
			if(pd.getUserid() == user.getId()) {
				paymentrepository.deleteById(pd.getSno());
			}
		}
		userrepository.deleteById(user.getId());
		String subject = "Notification: Account Deletion for Tourisum.jfsd.sdp.com";
		String htmlcontent ="<p><strong>Admin removed your Account</strong> </p>" +
				"<p><strong>Use below details  for further communication</strong> </p>" +
		        "<p><strong>Email:</strong>" + "2200032973@kluniversity.in"+ " </p>"+
		        "<p><strong>Telegram id:</strong>" + "@Mannava_Kamal"+ " </p>";		   
		sendmail(user.getEmail(), subject, htmlcontent);
		return 1;//successfully deleated
	}
	@GetMapping("/getallhoteladmins")
	public List<HotelAdmin> getallhoteladmins(HttpServletRequest request){
		if(checkadminsession1(request) == null ) {
			return null;
		}
		return hoteladminrepository.findAll();
	}
	@PostMapping("/deletehoteladminbyid")
	public Integer deletehoteladminbyid(@RequestBody HotelAdmin ha,HttpServletRequest request) throws Exception{
		if(checkadminsession1(request) == null ) {
			return null;
		}
		HotelAdmin ha1 = hoteladminrepository.findById(ha.getId()).get();
		if(ha1 == null) {
			return 0; // account not available 
		}if(ha.getStatus() == 0) {
			String subject = "Notification:  Rejection for Tourisum.jfsd.sdp.com";
			String htmlcontent ="<p><strong>Admin Rejected your Account creation request</strong> </p>" +
					"<p><strong>Use below details  for further communication</strong> </p>" +
			        "<p><strong>Email:</strong>" + "2200032973@kluniversity.in"+ " </p>"+
			        "<p><strong>Telegram id:</strong>" + "@Mannava_Kamal"+ " </p>";		   
			sendmail(ha.getEmail(), subject, htmlcontent);
		}
		else {
			List<RoomSchedule> lrs = roomschedulerepository.findAll();
			for(RoomSchedule rs : lrs) {
				if(rs.getHoteladminid() == ha.getId()) {
					roomschedulerepository.deleteById(rs.getOrderid());
				}
			}
			List<Rooms> lr = roomsrepository.findAll();
			for(Rooms r : lr) {
				if(r.getId() == ha.getId()) {
					roomsrepository.deleteById(r.getSno());
				}
			}
			String subject = "Notification:  Account removal for Tourisum.jfsd.sdp.com";
			String htmlcontent ="<p><strong>Admin removed your Account </strong> </p>" +
					"<p><strong>Use below details  for further communication</strong> </p>" +
			        "<p><strong>Email:</strong>" + "2200032973@kluniversity.in"+ " </p>"+
			        "<p><strong>Telegram id:</strong>" + "@Mannava_Kamal"+ " </p>";		   
			sendmail(ha.getEmail(), subject, htmlcontent);
		}
		hoteladminrepository.deleteById(ha.getId());
		return 1;//successfully deleated
	}

	
	@GetMapping("/adminlogout")
	public void adminlogout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("admindetails");
	}
	
	@Scheduled(fixedDelay = 600000)
	public void deleteroom_shedule() {
		LocalDateTime localDateTime = LocalDateTime.now();
	      ZonedDateTime istZonedDateTime = localDateTime.atZone(ZoneId.systemDefault())
                  .withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDateTime = istZonedDateTime.format(formatter);
        LocalDateTime dateTime1 = LocalDateTime.parse(formattedDateTime, formatter);// current IST time
		List<RoomSchedule> l1 =  roomschedulerepository.findAll();
		for(RoomSchedule rs : l1) {
			LocalDateTime dateTime2 = LocalDateTime.parse(rs.getCheckouttime(), formatter); // checkouttime in database
			if (dateTime2.isBefore(dateTime1)) {
	          roomschedulerepository.deleteById(rs.getOrderid());
	        }
		}
	}
	public void sendmail(String destinationemail,String subject,String htmlContent)throws Exception {
		try {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);        
        helper.setTo(destinationemail);
        helper.setSubject(subject);
        helper.setFrom("mannava.kamal@gmail.com");       
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage); 
		}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
	}
}


