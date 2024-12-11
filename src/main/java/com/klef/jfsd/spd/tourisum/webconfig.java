package com.klef.jfsd.spd.tourisum;

//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class webconfig implements WebMvcConfigurer {
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("*") // React frontend URL
	                .allowedMethods("GET", "POST", "PUT", "DELETE")
	                .allowCredentials(true); // Allow credentials (for cookies)
	    }
}
