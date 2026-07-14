package co.istad.rotana.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

// Structure project
// 1. Structure by layer
// 2. Structure by features
@SpringBootApplication
@EnableConfigurationProperties
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class Ecommerce {

	public static void main(String[] args) {
		SpringApplication.run(Ecommerce.class, args);
	}

}
