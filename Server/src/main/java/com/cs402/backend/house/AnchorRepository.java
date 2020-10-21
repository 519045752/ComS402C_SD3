package com.cs402.backend.house;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AnchorRepository extends CrudRepository<Anchor, Long> {
	
	
	@Query(value="select * from ar_project.anchor order by hid",nativeQuery=true)
	List<Anchor> getAnchorList();
	
	@Query(value="select * from ar_project.anchor where hid=?1",nativeQuery=true)
	List<Anchor> findAnchorbyHid(Long hid);
	
	@Query(value="select * from ar_project.anchor where aid=?1",nativeQuery=true)
	Anchor findAnchorbyAid(Long aid);
	
}