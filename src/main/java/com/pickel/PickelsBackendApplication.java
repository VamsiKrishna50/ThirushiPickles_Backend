package com.pickel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PickelsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickelsBackendApplication.class, args);
	}

}
