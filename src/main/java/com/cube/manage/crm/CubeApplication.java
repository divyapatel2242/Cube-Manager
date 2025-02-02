package com.cube.manage.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * @auther
 * Divya Patel
 * */

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAsync
public class CubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CubeApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
						.allowCredentials(true);
			}
		};
	}

}
