package com.tulgaa.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.tulgaa.model.MyUser;

public interface UserRepository extends DataTablesRepository<MyUser, Integer> {
	
}
