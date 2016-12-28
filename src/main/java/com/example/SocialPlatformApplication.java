package com.example;

import com.example.config.SrPlatformSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SrPlatformSettings.class})
public class SocialPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialPlatformApplication.class, args);
	}
}
