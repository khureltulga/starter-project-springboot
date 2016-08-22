package com.tulgaa.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Contains database configurations.
 */
@Configuration
@EnableTransactionManagement
@Import({ SecurityConfig.class })
public class DatabaseConfig extends WebMvcConfigurerAdapter{

  // ------------------------
  // PUBLIC METHODS
  // ------------------------

  /**
   * DataSource definition for database connection. Settings are read from
   * the application.properties file (using the env object).
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("db.driver"));
    dataSource.setUrl(env.getProperty("db.url"));
    dataSource.setUsername(env.getProperty("db.username"));
    dataSource.setPassword(env.getProperty("db.password"));
    return dataSource;
  }

  /**
   * Declare the JPA entity manager factory.
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactory =
        new LocalContainerEntityManagerFactoryBean();
    
    entityManagerFactory.setDataSource(dataSource);
    
    // Classpath scanning of @Component, @Service, etc annotated class
    entityManagerFactory.setPackagesToScan(
        env.getProperty("entitymanager.packagesToScan"));
    
    // Vendor adapter
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
    
    // Hibernate properties
    Properties additionalProperties = new Properties();
    additionalProperties.put(
        "hibernate.dialect", 
        env.getProperty("hibernate.dialect"));
    additionalProperties.put(
        "hibernate.show_sql", 
        env.getProperty("hibernate.show_sql"));
    additionalProperties.put("hibernate.connection.characterEncoding", "utf-8");
    additionalProperties.put("hibernate.enable_lazy_load_no_trans", env.getProperty("hibernate.enable_lazy_load_no_trans"));
/*    additionalProperties.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));*/
    additionalProperties.put("current_session_context_class", "tread");
    additionalProperties.put(
        "hibernate.hbm2ddl.auto", 
        env.getProperty("hibernate.hbm2ddl.auto"));
    entityManagerFactory.setJpaProperties(additionalProperties);
    
    return entityManagerFactory;
  }

  /**
   * Declare the transaction manager.
   */
  @Bean
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = 
        new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(
        entityManagerFactory.getObject());
    return transactionManager;
  }
  
  /**
   * PersistenceExceptionTranslationPostProcessor is a bean post processor
   * which adds an advisor to any bean annotated with Repository so that any
   * platform-specific exceptions are caught and then rethrown as one
   * Spring's unchecked data access exceptions (i.e. a subclass of 
   * DataAccessException).
   */
  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

 
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/izr/**").addResourceLocations("/angular/");
		registry.addResourceHandler("/uploads/**").addResourceLocations("/uploads/");
  }	
  
  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  @Autowired
  private Environment env;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private LocalContainerEntityManagerFactoryBean entityManagerFactory;

  @Bean
  public SessionFactory sessionFactory() {
	LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
	
	
    // Hibernate properties
    Properties additionalProperties = new Properties();
    additionalProperties.put(
        "hibernate.dialect", 
        env.getProperty("hibernate.dialect"));
    additionalProperties.put(
        "hibernate.show_sql", 
        env.getProperty("hibernate.show_sql"));
    additionalProperties.put(
        "hibernate.hbm2ddl.auto", 
        env.getProperty("hibernate.hbm2ddl.auto"));
/*    additionalProperties.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));*/
    additionalProperties.put("hibernate.enable_lazy_load_no_trans", env.getProperty("hibernate.enable_lazy_load_no_trans"));
    additionalProperties.put("current_session_context_class", "tread");
    
    builder
	.scanPackages(env.getProperty("entitymanager.packagesToScan"))
    .addProperties(additionalProperties);
    
	return builder.buildSessionFactory();
  }


} // class DatabaseConfig
