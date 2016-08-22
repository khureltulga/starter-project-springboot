package com.tulgaa;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class TulgaaApplication {

	public static String ROOT = "uploads";
	
	@Bean
	CommandLineRunner init() {
      return (String[] args) -> {
          new File(ROOT).mkdir();
      };
	}
	 
	public static void main(String[] args) {
		SpringApplication.run(TulgaaApplication.class, args);
	}
	
}
