package com.klef.jfsd.spd.tourisum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
public class SessionConfig {
	
	  @Bean
	    public DefaultCookieSerializer cookieSerializer() {
	        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
	        cookieSerializer.setSameSite("None");
	        cookieSerializer.setUseSecureCookie(true);
	        return cookieSerializer;
	    }

	    @Bean
	    public HttpSessionIdResolver httpSessionIdResolver() {
	        CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
	        resolver.setCookieSerializer(cookieSerializer());
	        return resolver;
	    }

	
}
