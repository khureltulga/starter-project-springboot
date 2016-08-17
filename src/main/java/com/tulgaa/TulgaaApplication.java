package com.tulgaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class TulgaaApplication {

	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(TulgaaApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TulgaaApplication.class, args);
	}
}
