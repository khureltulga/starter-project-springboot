package com.tulgaa.config;

import javax.servlet.Filter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	 
	@Autowired
	@Qualifier("MyUserDetailsService")
	UserDetailsService userDetailsService;

        
    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {    	
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
     
     
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            	.antMatchers("/","index.html","/package.json","/login","/user").permitAll()
            	.antMatchers("/app/**").permitAll()
            	.antMatchers("/assets/**").permitAll()
            	.antMatchers("/bower_components/**").permitAll()
            	.antMatchers("/data/**").permitAll()
            	.antMatchers("/file_manager/**").permitAll()
            	.antMatchers("/gulp-tasks/**").permitAll()
            	.antMatchers("/app/**").permitAll()
            	.antMatchers("/api/**").permitAll()
            	.anyRequest()
				.authenticated()
				.and()
				    .logout()		
				    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				    .logoutSuccessUrl("/login?logout")                 
					.invalidateHttpSession(true)                                    
					.deleteCookies("JSESSIONID") 
					.logoutSuccessUrl("/login")
				.and()
				   .sessionManagement()
	               .invalidSessionUrl( "/#/login" )
	               .sessionAuthenticationErrorUrl("/login")
	               .maximumSessions( 1 )
	               .expiredUrl("/login")
	               .and()
	            .and()
				.csrf().disable();
    }
}