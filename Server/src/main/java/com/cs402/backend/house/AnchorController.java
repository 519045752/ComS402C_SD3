package com.cs402.backend.house;

import com.cs402.backend.respond.RespondCodeEnum;
import com.cs402.backend.respond.RespondJson;
import com.cs402.backend.user.User;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/anchor")
@Api(value = "/anchor ", tags = {"API collection of anchors"})
public class AnchorController {
	
	@Autowired
	HouseRepository houseRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AnchorRepository anchorRepository;
	private static final Logger log = LoggerFactory.getLogger(AnchorController.class);
	private static final String greeting = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@GetMapping("/all")
	@ApiOperation(value = "get list of information of all anchors", notes = "test only")
	public RespondJson<Iterable<Anchor>> getAllAnchors() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, anchorRepository.findAll());
	}
	
	@GetMapping("/{aid}")
	@ApiOperation(value = "visit info page of an anchor", notes = "")
	@ApiImplicitParam(name = "aid", value = "anchor id", required = true, dataType = "Long")
	public Object hidPage(@PathVariable Long aid) {
		try {
			Anchor anchor = anchorRepository.findAnchorbyAid(aid);
			RespondJson<Anchor> ret = RespondJson.out(RespondCodeEnum.SUCCESS, anchor);
			return ret;
		} catch (IndexOutOfBoundsException e) {
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
	@ApiOperation(value = "add anchor to a house")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hid", value = "house id", required = true, paramType = "query", dataType = "long"),
			@ApiImplicitParam(name = "type", value = "type of icon/object", required = true, paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "description", value = "text info", required = true, paramType = "query", dataType = "string")
	})
	public Object addAnchor(@RequestParam Long hid, @RequestParam int type, @RequestParam String description) {
		if (!checkHouseExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			Anchor anchor = new Anchor();
			House house = this.houseRepository.getHouseByHid(hid);
			anchor.setHouse(house);
			anchor.setType(type);
			anchor.setDescription(description);
			anchorRepository.save(anchor);
			return RespondJson.out(RespondCodeEnum.SUCCESS, anchor);
		}
	}
	
	@PostMapping(path = "/setInfo")
	@ApiOperation(value = "change the info of an anchor")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "aid", value = "anchor id", required = true, paramType = "query", dataType = "long"),
			@ApiImplicitParam(name = "type", value = "type of icon/object", paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "description", value = "text info", paramType = "query", dataType = "String")
	})
	public Object setAnchor(@RequestParam Long aid, Integer type, String description) {
		if (!checkAnchorExist(aid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			Anchor anchor = this.anchorRepository.findAnchorbyAid(aid);
			if(type != null){
				anchor.setType(type);
			}
			if(description != null){
				anchor.setDescription(description);
			}
			
			anchorRepository.save(anchor);
			return RespondJson.out(RespondCodeEnum.SUCCESS, anchor);
		}
	}
	
	@PostMapping(path = "/getHouseAnchors")
	@ApiOperation(value = "get the anchor list of a house")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hid", value = "house id", required = true, paramType = "query", dataType = "long")
	})
	public Object getAnchorList(@RequestParam Long hid) {
		if (!checkHouseExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
		}
		else {
			List<Anchor> list = this.anchorRepository.findAnchorbyHid(hid);
			return RespondJson.out(RespondCodeEnum.SUCCESS, list);
		}
	}
	
	
	@PostMapping(path = "/getAnchor")
	@ApiOperation(value = "get the info of the certain anchor")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "aid", value = "anchor id", required = true, paramType = "query", dataType = "long")
	})
	public Object getAnchor(@RequestParam Long aid) {
		if (!checkAnchorExist(aid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			Anchor anchor = this.anchorRepository.findAnchorbyAid(aid);
			return RespondJson.out(RespondCodeEnum.SUCCESS, anchor);
		}
	}
	
	@PostMapping(path = "/remove")
	@ApiOperation(value = "remove the anchor by aid")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "aid", value = "anchor id", required = true, paramType = "query", dataType = "long")
	})
	public Object removeAnchor(@RequestParam Long aid) {
		if (!checkAnchorExist(aid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Aid_NOT_FOUND);
		}
		else {
			this.anchorRepository.deleteById(aid);
			return RespondJson.out(RespondCodeEnum.SUCCESS);
		}
	}
	// @PostMapping(path = "/setAnchor")
	// @ApiOperation(value = "change the info of the certain anchor")
	// @ApiImplicitParams({
	// 		@ApiImplicitParam(name = "aid", value = "anchor id", required = true, paramType = "query", dataType = "long"),
	// 		@ApiImplicitParam(name = "type", value = "type of icon/object",  paramType = "query", dataType = "int"),
	// 		@ApiImplicitParam(name = "description", value = "text info",  paramType = "query", dataType = "string")
	// })
	// public Object setAnchor(@RequestParam Long aid, int type, String description) {
	// 	if (!checkAnchorExist(aid)) {
	// 		return RespondJson.out(RespondCodeEnum.FAIL_Hid_NOT_FOUND);
	// 	}
	// 	else {
	// 		Anchor anchor = anchorSet.iterator();
	// 		anchor.setType(type);
	// 		anchor.setDescription(description);
	//
	// 		anchorSet.add(anchor);
	// 		house.setAnchors(anchorSet);
	// 		houseRepository.save(house);
	// 		return RespondJson.out(RespondCodeEnum.SUCCESS, anchor);
	// 	}
	// }
	
	public Boolean checkAnchorExist(Long aid) {
		Anchor ancbor = this.anchorRepository.findAnchorbyAid(aid);
		return !(ancbor == null);
	}
}
