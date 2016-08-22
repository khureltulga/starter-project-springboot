package com.tulgaa.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tulgaa.model.MyUser;
import com.tulgaa.model.UserDao;

@Service("MyUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	 @Autowired
	private UserDao userDao;

	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		System.out.println("bla bla");
		MyUser user = userDao.getByEmail(username);
		List<GrantedAuthority> authorities = buildUserAuthority();

		return buildUserForAuthentication(user, authorities);
		
	}

	private User buildUserForAuthentication(MyUser user, List<GrantedAuthority> authorities) {
		return new User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority() {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		setAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}

}