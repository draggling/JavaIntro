  
package com.ss.lms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import com.ss.lms.service.AdministratorService;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {
		 ErrorMvcAutoConfiguration.class
		})
@PropertySource("classpath:application.properties")
public class LmsAdminApplication {
	
	@Autowired
	AdministratorService adminService;

	public static void main(String[] args) {
		SpringApplication.run(LmsAdminApplication.class, args);
	}
}