  
package com.ss.lms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.ss.lms.service.AdministratorService;


@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.ss.lms, com.ss.lms.dao, com.ss.lms.entity")


public class LibraryApplication {
	
	@Autowired
	AdministratorService adminService;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
}