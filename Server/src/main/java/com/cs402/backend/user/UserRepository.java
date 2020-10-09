package com.cs402.backend.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	@Query(value="select username from ar_project.user order by uid",nativeQuery=true)
	List<String> getUserlist();
	
	// @Query(value="select username from ar_project.user order by uid",nativeQuery=true)
	// public List<Map<String, Object>> getUserlist(Integer n);
	@Query(value="select * from ar_project.user where username=?1" ,nativeQuery = true)
	List<User> checkIfExist(String username);
	
	@Query(value="select * from ar_project.user where username=?1 and password=?2",nativeQuery=true)
	List<User> login(String username, String password);
	
	@Query(value="select * from ar_project.user where uid=?1 and password=?2",nativeQuery=true)
	List<User> loginByUid(Long uid, String password);
	
	@Query(value="select * from ar_project.user where uid=?1" ,nativeQuery = true)
	List<User> findUserById(Long uid);
	
	@Query(value="select uid from ar_project.user where username=?1" ,nativeQuery = true)
	public Long findUidByUsername(String username);
	
	@Query(value="update email from ar_project.user where username=?1" ,nativeQuery = true)
	public Long setEmail(String username);
	
	
	
}