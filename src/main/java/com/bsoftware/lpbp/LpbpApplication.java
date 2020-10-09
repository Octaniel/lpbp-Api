package com.bsoftware.lpbp;

import com.bsoftware.lpbp.config.property.SpringApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(SpringApiProperty.class)
@SpringBootApplication
public class LpbpApplication {
	public static void main(String[] args) {
		SpringApplication.run(LpbpApplication.class, args);
	}

}
