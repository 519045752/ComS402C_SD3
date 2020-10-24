package com.cs402.backend.house;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ArObjectRepository extends CrudRepository<ArObject, Long> {
	
	
	@Query(value="select * from ar_project.ar_object order by hid",nativeQuery=true)
	List<ArObject> getAnchorList();
	
	@Query(value="select * from ar_project.ar_object where hid=?1",nativeQuery=true)
	List<ArObject> findObjectByHid(Long hid);
	
	@Query(value="select * from ar_project.ar_object where oid=?1",nativeQuery=true)
	ArObject findObjectByOid(Long oid);
	
	@Query(value="select * from ar_project.ar_object where cloudid=?1",nativeQuery=true)
	ArObject findObjectByCloudId(String cloudid);
	
	@Query(value="delete from ar_project.ar_object;", nativeQuery=true)
	ArObject removeAllObjects();
}