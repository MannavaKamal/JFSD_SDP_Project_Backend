package com.klef.jfsd.spd.tourisum.controller;


import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klef.jfsd.spd.tourisum.model.HotelAdmin;
import com.klef.jfsd.spd.tourisum.model.RoomSchedule;
import com.klef.jfsd.spd.tourisum.model.Rooms;
import com.klef.jfsd.spd.tourisum.model.User;
import com.klef.jfsd.spd.tourisum.repository.AdminRepository;
import com.klef.jfsd.spd.tourisum.repository.HotelAdminRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomScheduleRepository;
import com.klef.jfsd.spd.tourisum.repository.RoomsRepository;
import com.klef.jfsd.spd.tourisum.repository.UserRepository;
import com.klef.jfsd.spd.tourisum.service.HotelAdminService;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@RestController
public class HotelAdminController {
	@Autowired
	  private JavaMailSender mailSender;
	
	@Autowired
	private HotelAdminService hoteladminservice;
	@Autowired
	private AdminRepository adminrepo;
	@Autowired
	private HotelAdminRepository hoteladminrepo;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private RoomScheduleRepository roomschedulerepository;
	@Autowired
	private RoomsRepository roomsrepository;
	
	
	@GetMapping("/checkhoteladminsession")
	  public HotelAdmin checkhoteladminsession(HttpServletRequest request) {
		  HttpSession session = request.getSession();
	    	HotelAdmin ha = (HotelAdmin)session.getAttribute("hoteladmindetails");
	    	if(ha!=null) {
	    		session.setMaxInactiveInterval(3600);
	    		return ha;
	    	}else {
	    		return null;
	    	}
	    }
	@GetMapping("/checkhoteladminsession1")
	  public HotelAdmin checkhoteladminsession1(HttpServletRequest request) {
		  HttpSession session = request.getSession();
	    	HotelAdmin ha = (HotelAdmin)session.getAttribute("hoteladmindetails");
	    	if(ha!=null) {
	    		return ha;
	    	}else {
	    		return null;
	    	}
	    }
	    @GetMapping("/checkhoteladminsignupsession")
	    public HotelAdmin checkhoteladminsignupsession(HttpServletRequest request) {
	    	HttpSession session = request.getSession();
	    	HotelAdmin ha = (HotelAdmin)session.getAttribute("hoteladminsignup");
	    	
	    	if(ha!=null) {
	    		return ha;
	    	}else {
	    		return null;
	    	}
	    }
	    
	    @GetMapping("/removehoteladminsignupsession")
	    public void removehoteladminsignupsession(HttpServletRequest request) {
	    	HttpSession session = request.getSession();    	
	    	session.removeAttribute("hoteladminsignup");
	    }
	
	@PostMapping("/insertHotelAdmin")
	public String insert(@RequestBody HotelAdmin h1,HttpServletRequest request) throws Exception  {		
		if(userrepo.checkUserByEmail(h1.getEmail())!=null || hoteladminrepo.checkHotelAdminByEmail(h1.getEmail())!=null || adminrepo.checkAdminByEmail(h1.getEmail())!=null) {
			return "2";
		}					
    	HttpSession session =  request.getSession();
    	session.removeAttribute("hoteladmindetails");
    	session.removeAttribute("pay");
    	session.removeAttribute("admindetails");
    	session.removeAttribute("user");    	
    	session.setMaxInactiveInterval(600);
    	session.setAttribute("hoteladminsignup", h1);
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);        
        int otp = (int)(Math.random() * 99999); // random number generation
        session.setAttribute("otp", otp);
        helper.setTo(h1.getEmail());
        helper.setSubject("OTP from Tourisum.jfsd.sdp.com");
        helper.setFrom("mannava.kamal@gmail.com");
        String htmlContent =         
        "<p><strong>OTP:</strong> " + otp + "</p>" +
        "<p><strong>Note:</strong>" + "this otp expires in 10 minutes"+ " </p>";
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);    	
		return "1";
		
	}
	
	@PostMapping("/checkotp")
	public String checkotp(@RequestBody HotelAdmin h1,HttpServletRequest request) throws Exception  {
		
		if (checkhoteladminsignupsession(request)==null)
		{
			return null;
		}
		HttpSession session = request.getSession();
    	HotelAdmin h2 = (HotelAdmin)session.getAttribute("hoteladminsignup");
    	int otp = (int)session.getAttribute("otp");
    	if( h1.getId()==otp) {
        hoteladminservice.inserthoteladmin(h2);
    		session.removeAttribute("otp");
    		session.removeAttribute("hoteladminsignup");
      return "1";
    	}
    	return "0";	
	}
	 @PostMapping("posthoteladminbyid")
		public HotelAdmin gethoteladminbyid(@RequestBody HotelAdmin ha,HttpServletRequest request) {
	    	 HotelAdmin ha1 = checkhoteladminsession1(request);
			if(ha1 == null ) {
				return null;
			}
			if(ha1.getId() != ha.getId()) {
				ha.setName("");
				return ha;
			}
			ha = hoteladminrepo.findById(ha.getId()).get();
			if(ha == null) {   // if user is in session and if account deleted 
				ha1.setName(" ");
				return ha1;
			}
		return  ha;
		}
	
	 @PostMapping("/hoteladminprofileupdate")
	    public Integer hoteladminprofileupdate(@RequestBody HotelAdmin ha1, HttpServletRequest request)
	    {
	    	HotelAdmin ha = checkhoteladminsession1(request);
	    	if(ha==null) {
	    		return null;
	    	}
	        HotelAdmin ha2 = hoteladminrepo.findById(ha1.getId()).get();
	        if(ha2 == null) {
	        	return 0;
	        }
	        ha2.setHotelname(ha1.getHotelname());
	        ha2.setName(ha1.getName());
	        ha2.setContact(ha1.getContact());
	        ha2.setSex(ha1.getSex());
	        ha2.setHotelimageinbytes(ha1.getHotelimageinbytes());
	        hoteladminrepo.save(ha2);
	    	return 1;
	    }
	
	@PostMapping("/HotelAdminAddRoom")
	public Integer AddRooms(@RequestBody Rooms r1,HttpServletRequest request) 
	{
	HotelAdmin	ha =checkhoteladminsession1(request);
	if(ha == null) 
	{	
		return null;
	}
	Rooms r2 = hoteladminservice.checkroom(ha.getId(), r1.getRoomno());
	if(r2 == null) {
	r1.setId(ha.getId());
	hoteladminservice.hoteladminaddrooms(r1);	
	return 1; //"Room Added SuccessFully"
	}
	else {
		return 0;  //"Room Already exists"
	}	
	}
	
	@GetMapping("/HotelAdminRooms")
	public List<Rooms> hoteladminrooms(HttpServletRequest request)
	{
		HotelAdmin	ha =checkhoteladminsession1(request);
		if(ha == null) 
		{
		   return null;	
		}
			List<Rooms> lr = hoteladminservice.getAllRoomsById(ha.getId());
			return lr;
	}
	
	@PostMapping("/deleteroom")
	public Integer deleteroom(@RequestBody Rooms r1, HttpServletRequest request)
	{
		HotelAdmin ha = checkhoteladminsession1(request);
		if(ha == null) {
			return null;
		}
		Rooms r2 =  hoteladminservice.getRoomBySno(r1.getSno());
		if(r2 == null) {
			return 0;
		}
		roomsrepository.deleteById(r1.getSno());
			return 1;
		
	}
	@PostMapping("/updateroom")
	public Integer updateroom(@RequestBody Rooms r1, HttpServletRequest request)
	{
		HotelAdmin ha = checkhoteladminsession1(request);
		if(ha == null) {
			return null;
		}
		Rooms r2 =  roomsrepository.findById(r1.getSno()).get();
		if(r2 == null) {
			return 0;
		}
		r2.setRoomtype(r1.getRoomtype());
		r2.setRoomcost(r1.getRoomcost());
		r2.setRoomimageinbytes(r2.getRoomimageinbytes());
		roomsrepository.save(r2);
		return 1;
	}
	 
	@GetMapping("/roombookingsbasedonhoteladminid")
	public List<RoomSchedule> roombookingsbasedonhoteladminid(HttpServletRequest request){
		HotelAdmin ha = checkhoteladminsession1(request);
		if(ha == null) {
			return null;
		}
		
		return roomschedulerepository.roomidbasedonhoteladminid(ha.getId()); // all booking rooms based on hoteladmin id
	}
	
	@PostMapping("senduserbyid")
	public User getuserbyid(@RequestBody User user,HttpServletRequest request) {
		if(checkhoteladminsession1(request) == null ) {
			return null;
		}
	return userrepo.getUserById(user.getId());	
	}
	@PostMapping("/sendroombyid")
	public Rooms getroombyid(@RequestBody Rooms r1, HttpServletRequest request) { 
		HotelAdmin ha = checkhoteladminsession1(request);
		if(ha == null){
			return null;
		}	
	Optional<Rooms> r2 =	roomsrepository.findById(r1.getSno());
		return r2.get();
	}
	@GetMapping("/hoteladminlogout")// removing the session attribute
	public void lhoteladminogout(HttpServletRequest request) 
	{		
		if (checkhoteladminsession1(request)!=null) {
			HttpSession session = request.getSession();
			session.removeAttribute("hoteladmindetails");
		}	
	}
	
	
}
