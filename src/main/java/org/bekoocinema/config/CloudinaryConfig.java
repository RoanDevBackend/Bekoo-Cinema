package org.bekoocinema.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    String cloud_name;
    @Value("${cloudinary.api_key}")
    String api_key;
    @Value("${cloudinary.api_secret}")
    String api_secret;

    @Bean
    public Cloudinary cloudinary(){
        Map<Object , Object> map = new HashMap<>() ;
        map.put("cloud_name" , cloud_name) ;
        map.put("api_key" , api_key) ;
        map.put("api_secret" , api_secret) ;
        map.put("secure" , true) ;
        return new Cloudinary(map) ;
    }
}
