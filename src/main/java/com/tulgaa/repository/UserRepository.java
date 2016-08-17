package com.tulgaa.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.tulgaa.model.User;

public interface UserRepository extends DataTablesRepository<User, Integer> {
	
}
