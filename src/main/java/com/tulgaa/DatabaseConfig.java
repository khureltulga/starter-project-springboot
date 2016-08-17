package com.tulgaa;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

  @Value("${db.driver}")
  private String DB_DRIVER;
  
  @Value("${db.password}")
  private String DB_PASSWORD;
  
  @Value("${db.url}")
  private String DB_URL;
  
  @Value("${db.username}")
  private String DB_USERNAME;

  @Value("${hibernate.dialect}")
  private String HIBERNATE_DIALECT;
  
  @Value("${hibernate.show_sql}")
  private String HIBERNATE_SHOW_SQL;
  
  @Value("${hibernate.hbm2ddl.auto}")
  private String HIBERNATE_HBM2DDL_AUTO;

  @Value("${entitymanager.packagesToScan}")
  private String ENTITYMANAGER_PACKAGES_TO_SCAN;
  
  /*@Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(DB_DRIVER);
    dataSource.setUrl(DB_URL);
    dataSource.setUsername(DB_USERNAME);
    dataSource.setPassword(DB_PASSWORD);
    return dataSource;
  }*/

  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource());
    sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
    hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
    hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
    sessionFactoryBean.setHibernateProperties(hibernateProperties);
    
    return sessionFactoryBean;
  }

/*  @Bean
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager transactionManager = 
        new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory().getObject());
    return transactionManager;
  }*/
  
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


  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  @Autowired
  private Environment env;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private LocalContainerEntityManagerFactoryBean entityManagerFactory;

}