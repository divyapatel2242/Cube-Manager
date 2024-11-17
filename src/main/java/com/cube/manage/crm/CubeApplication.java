package com.cube.manage.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * @auther
 * Divya Patel
 * */

@SpringBootApplication
@EnableAsync
public class CubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubeApplication.class, args);
	}

}
