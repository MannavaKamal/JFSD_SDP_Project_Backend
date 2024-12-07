package com.klef.jfsd.spd.tourisum.model;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
@Entity
@Table(name="HotelAdmin")
public class HotelAdmin {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY) // you can take this manually also through form
	 @Column(name="id")
	 private int id;
	 @Column(name="name",nullable=false,length = 50)
	 private String name;
	 @Column(name="email",nullable=false,unique = true,length = 50)
	 private String email;
	 @Column(name="password",nullable=false,length = 50)
	 private String password;
	 @Column(name="sex",nullable=false,length = 50)
	 private String sex;
	 @Column(name="contact",nullable=false,length = 50)
	 private long contact;
	 @Column(name="country",nullable=false,length=50)
	 private String country;
	 @Column(name="state",nullable=false,length=50)
	 private String state;
	 @Column(name="city",nullable=false,length=50)
	 private String city;
	 @Column(name="hotelname",nullable=false, unique = true,length=50)
	 private String hotelname;
	 @Lob
	 @Column(name = "hotelimageinbytes", nullable = false,columnDefinition = "MEDIUMTEXT")
	private String hotelimageinbytes;
	 @Column(name = "status")
	 private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	public String getHotelimageinbytes() {
		return hotelimageinbytes;
	}
	public void setHotelimageinbytes(String hotelimageinbytes) {
		this.hotelimageinbytes = hotelimageinbytes;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "HotelAdmin [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", sex="
				+ sex + ", contact=" + contact + ", country=" + country + ", state=" + state + ", city=" + city
				+ ", hotelname=" + hotelname + ", hotelimageinbytes=" + hotelimageinbytes + "]";
	}
	
	 
	
}
