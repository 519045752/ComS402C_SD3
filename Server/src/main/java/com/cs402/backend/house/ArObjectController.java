package com.cs402.backend.house;

import com.cs402.backend.respond.RespondCodeEnum;
import com.cs402.backend.respond.RespondJson;
import com.cs402.backend.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/arObject")
@Api(value = "/arObject ", tags = {"API collection of arObjects"})
public class ArObjectController {
	
	@Autowired
	HouseRepository houseRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ArObjectRepository arObjectRepository;
	private static final Logger log = LoggerFactory.getLogger(ArObjectController.class);
	private static final String greeting = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@GetMapping("/all")
	@ApiOperation(value = "get list of information of all arObjects", notes = "test only")
	public RespondJson<Iterable<ArObject>> getAllArObjects() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, arObjectRepository.findAll());
	}
	
	@GetMapping("/{oid}")
	@ApiOperation(value = "visit info page of an arObject", notes = "")
	@ApiImplicitParam(name = "oid", value = "arObject id", required = true, dataType = "Long")
	public Object hidPage(@PathVariable Long oid) {
		try {
			ArObject arObject = arObjectRepository.findObjectByOid(oid);
			RespondJson<ArObject> ret = RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
			return ret;
		} catch (IndexOutOfBoundsException|NullPointerException e) {
			e.printStackTrace();
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			RespondCodeEnum res = RespondCodeEnum.FAIL;
			res.setMgs(e.toString());
			return RespondJson.out(res);
		}
	}
	
	
	

	
	public boolean checkHouseExist(Long hid){
		House house = this.houseRepository.getHouseByHid(hid);
		return !(house==null);
	}
	
	@PostMapping(path = "/add")
	@ApiOperation(value = "add arObject to a house")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hid", value = "house id", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "cloudid", value = "cloud id", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "type", value = "type of icon/object", required = true, paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "description", value = "text info", required = true, paramType = "query", dataType = "String")
	})
	public Object addArObject(@RequestParam Long hid, @RequestParam String cloudid,@RequestParam int type, @RequestParam String description) {
		if (!checkHouseExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			ArObject arObject = new ArObject();
			House house = this.houseRepository.getHouseByHid(hid);
			arObject.setHouse(house);
			arObject.setType(type);
			arObject.setCloudid(cloudid);
			arObject.setDescription(description);
			arObjectRepository.save(arObject);
			return RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
		}
	}
	
	@PostMapping(path = "/setInfo")
	@ApiOperation(value = "change the info of an arObject")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oid", value = "arObject id", required = true, paramType = "query", dataType = "long"),
			@ApiImplicitParam(name = "cloudid", value = "cloud id", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "type", value = "type of icon/object", paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "description", value = "text info", paramType = "query", dataType = "String")
	})
	public Object setArObject(@RequestParam Long oid,String cloudid, Integer type, String description) {
		if (!checkArObjectExist(oid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			ArObject arObject = this.arObjectRepository.findObjectByOid(oid);
			if(type != null){
				arObject.setType(type);
			}
			if(description != null){
				arObject.setDescription(description);
			}
			if(cloudid != null){
				arObject.setCloudid(cloudid);
			}
			arObjectRepository.save(arObject);
			return RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
		}
	}
	
	@GetMapping(path = "/getHouseObjects")
	@ApiOperation(value = "get the object list from a house")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hid", value = "house id", required = true, paramType = "query", dataType = "long")
	})
	public Object getArObjectList(@RequestParam Long hid) {
		if (!checkHouseExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			List<ArObject> list = this.arObjectRepository.findObjectByHid(hid);
			return RespondJson.out(RespondCodeEnum.SUCCESS, list);
		}
	}
	
	
	@GetMapping(path = "/getObject")
	@ApiOperation(value = "get the info of an object through oid")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oid", value = "object id", required = true, paramType = "query", dataType = "long")
	})
	public Object getArObject(@RequestParam Long oid) {
		if (!checkArObjectExist(oid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			ArObject arObject = this.arObjectRepository.findObjectByOid(oid);
			return RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
		}
	}
	
	@GetMapping(path = "/getObjectByCloudId")
	@ApiOperation(value = "get the info of an object through cloud id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cloudid", value = "cloud id", required = true, paramType = "query", dataType = "string")
	})
	public Object getArObject(@RequestParam String cloudid) {
		if (!checkArObjectExist(cloudid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			ArObject arObject = this.arObjectRepository.findObjectByCloudId(cloudid);
			return RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
		}
	}
	
	@PostMapping(path = "/remove")
	@ApiOperation(value = "remove the arObject by oid")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oid", value = "arObject id", required = true, paramType = "query", dataType = "long")
	})
	public Object removeArObject(@RequestParam Long oid) {
		if (!checkArObjectExist(oid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			this.arObjectRepository.deleteById(oid);
			return RespondJson.out(RespondCodeEnum.SUCCESS);
		}
	}
	
	@PostMapping(path = "/removeByCloudID")
	@ApiOperation(value = "remove the arObject by cloudID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cloudID", value = "arObject cloudID", required = true, paramType = "query", dataType = "string")
	})
	public Object removeByCloudID(@RequestParam String cloudID) {
		if (!checkArObjectExistByCloudID(cloudID)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			ArObject anchor = this.arObjectRepository.findObjectByCloudId(cloudID);
			this.arObjectRepository.deleteById(anchor.getOid());
			return RespondJson.out(RespondCodeEnum.SUCCESS);
		}
	}
	
	@PostMapping(path = "/removeAll")
	@ApiOperation(value = "remove ALL the arObject")
	public Object removeAll() {
			arObjectRepository.deleteAll();
			return RespondJson.out(RespondCodeEnum.SUCCESS);
	}
	// @PostMapping(path = "/setArObject")
	// @ApiOperation(value = "change the info of the certain arObject")
	// @ApiImplicitParams({
	// 		@ApiImplicitParam(name = "oid", value = "arObject id", required = true, paramType = "query", dataType = "long"),
	// 		@ApiImplicitParam(name = "type", value = "type of icon/object",  paramType = "query", dataType = "int"),
	// 		@ApiImplicitParam(name = "description", value = "text info",  paramType = "query", dataType = "string")
	// })
	// public Object setArObject(@RequestParam Long oid, int type, String description) {
	// 	if (!checkArObjectExist(oid)) {
	// 		return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
	// 	}
	// 	else {
	// 		ArObject arObject = arObjectSet.iterator();
	// 		arObject.setType(type);
	// 		arObject.setDescription(description);
	//
	// 		arObjectSet.add(arObject);
	// 		house.setArObjects(arObjectSet);
	// 		houseRepository.save(house);
	// 		return RespondJson.out(RespondCodeEnum.SUCCESS, arObject);
	// 	}
	// }
	
	public Boolean checkArObjectExist(Long oid) {
		ArObject ancbor = this.arObjectRepository.findObjectByOid(oid);
		return !(ancbor == null);
	}
	
	public Boolean checkArObjectExistByCloudID(String cloudID) {
		ArObject ancbor = this.arObjectRepository.findObjectByCloudId(cloudID);
		return !(ancbor == null);
	}
	
	public Boolean checkArObjectExist(String cloudid) {
		ArObject ancbor = this.arObjectRepository.findObjectByCloudId(cloudid);
		return !(ancbor == null);
	}
}
