package com.klef.jfsd.spd.tourisum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableScheduling
@EnableRedisHttpSession	
public class TourisumApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourisumApplication.class, args);
		System.out.println("Tourism Project is Running");
		
	}

}
