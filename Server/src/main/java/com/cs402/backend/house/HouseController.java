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
@RequestMapping("/house")
@Api(value = "/house ", tags = {"API collection of houses"})
public class HouseController {
	
	@Autowired
	HouseRepository houseRepository;
	@Autowired
	UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(HouseController.class);
	private static final String greeting = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@GetMapping("/all")
	@ApiOperation(value = "get list of information of all houses", notes = "test only")
	public RespondJson<Iterable<House>> getAllHouse() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, houseRepository.findAll());
	}
	
	@GetMapping("/{hid}")
	@ApiOperation(value = "visit info page of houses", notes = "")
	@ApiImplicitParam(name = "hid", value = "house id", required = true, dataType = "Long")
	public Object hidPage(@PathVariable Long hid) {
		try {
			House house = houseRepository.getHouseByHid(hid);
			RespondJson<House> ret = RespondJson.out(RespondCodeEnum.SUCCESS, house);
			return ret;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return RespondJson.out(RespondCodeEnum.FAIL_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			RespondCodeEnum res = RespondCodeEnum.FAIL;
			res.setMgs(e.toString());
			return RespondJson.out(res);
		}
	}
	
	
	@PostMapping(path = "/add")
	@ApiOperation(value = "register for a new house to rent")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uidLandlord", value = "uid of the landlord", required = true, paramType = "query", dataType = "long"),
			@ApiImplicitParam(name = "address", value = "address of the house", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "latitude", value = "latitude of the house", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "longitude", value = "longitude of the house", required = true, paramType = "query", dataType = "string"),
			
			@ApiImplicitParam(name = "addressOpt", value = "optional address", paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "price", value = "rental", paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "info", value = "information about the house", paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "data", value = "AR data to be stored", paramType = "query", dataType = "string")
	})
	public Object addHouse(
			@RequestParam Long uidLandlord, @RequestParam String address, @RequestParam String latitude, @RequestParam String longitude,
			String addressOpt, String price, String info, String data) {
		if (!checkUserExist(uidLandlord)) {
			return RespondJson.out(RespondCodeEnum.FAIL_NOT_FOUND);
		}
		else {
			House house = new House();
			User landlord = userRepository.findUserById(uidLandlord);
			house.setLandlord(landlord);//TODO set landlord
			house.setAddress(address);
			house.setLatitude(latitude);
			house.setLongitude(longitude);
			if (price == null) {
				house.setPrice("TBA");
			}
			else {
				house.setPrice(price);
			}
			house.setAddressOpt(addressOpt);
			house.setData(data);
			house.setInfo(info);
			houseRepository.save(house);
			return RespondJson.out(RespondCodeEnum.SUCCESS, house);
		}
	}
	
	public Boolean checkUserExist(Long uid) {
		User user = null;
		try {
			user = this.userRepository.findUserById(uid);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@PostMapping(path = "/addTenant")
	@ApiOperation(value = "add an user to the house as a tenant")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hid", value = "house id", required = true, paramType = "query", dataType = "long"),
			@ApiImplicitParam(name = "uid", value = "tenant id to be added", required = true, paramType = "query", dataType = "long")
	})
	public Object addTenant(
			@RequestParam Long hid, @RequestParam Long uid) {
		if (!checkHouseExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_NOT_FOUND);
		}
		if (!checkUserExist(hid)) {
			return RespondJson.out(RespondCodeEnum.FAIL_NOT_FOUND);
		}
		else {
			House house = this.houseRepository.getHouseByHid(hid);
			User user = this.userRepository.findUserById(uid);
			Set<User> tenant = house.getTenant();
			tenant.add(user);
			houseRepository.save(house);
			return RespondJson.out(RespondCodeEnum.SUCCESS, house);
		}
	}
	
	public boolean checkHouseExist(Long hid){
		House house = this.houseRepository.getHouseByHid(hid);
		return !(house==null);
	}
}
