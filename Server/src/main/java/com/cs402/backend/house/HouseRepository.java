package com.cs402.backend.house;

import com.cs402.backend.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface HouseRepository extends CrudRepository<House, Long> {
	@Query(value="select * from ar_project.house order by hid",nativeQuery=true)
	List<String> getHouselist();
	
	@Query(value="select * from ar_project.house where hid=?1" ,nativeQuery = true)
	House getHouseByHid(Long Long);
	
	// @Query(value="select username from ar_project.user order by uid",nativeQuery=true)
	// public List<Map<String, Object>> getUserlist(Integer n);
	@Query(value="select * from ar_project.house where uidLandlord=?1" ,nativeQuery = true)
	List<House> getHouseListByLandlord(Long Long);
	
	@Query(value="select * from ar_project.house where uidTenant=?1" ,nativeQuery = true)
	List<House> getHouseListByBOrderByUidTenant(Long Long);
	
	
	
}