package com.sgu.caro.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sgu.caro.entity.User;
import com.sgu.caro.exception.ResourceNotFoundException;
import com.sgu.caro.repository.UserRepository;

@RestController
@RequestMapping("/caro_api/")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	// Get all employee
	//@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("users")
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	};
	
	// Get employee by id
	//@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return ResponseEntity.ok().body(user);
	};
	
	// Create new employee
	//@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("users")
	public User createUser(@Valid @RequestBody User user) {
		String dateString = user.getDayOfBirth();
		String gender = user.getGender();
		String pass = user.getPassword();
		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
		String regex = "([0-9]{4})/([0-9]{2})/([0-9]{2})";
		if(!dateString.matches(regex)) {
			user.setDayOfBirth(null);
		};
		if(!gender.equals("male") && !gender.equals("female") && !gender.equals("undefiend")) {
			user.setGender("undefiend");
		};
		String hashPass = pbkdf2PasswordEncoder.encode(pass);
		user.setPassword(hashPass);
		user.setScore(0);
		return userRepository.save(user);
	};
	
	// Update smart phone by id
	//@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") long userId, @Valid @RequestBody User updateUser) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		String gender = updateUser.getGender();
		int score = updateUser.getScore();
		if(!gender.equals("male") && !gender.equals("female") && !gender.equals("undefiend")) {
			user.setGender("undefiend");
		};
		if(score > 0) {
			user.setScore(updateUser.getScore());
		}else {
			user.setScore(0);
		};
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		User editUser = userRepository.save(user);
		return ResponseEntity.ok().body(editUser);
	};
	
	// Delete smart phone by id
	//@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("users/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		this.userRepository.delete(user);
		Map<String, Boolean> respone = new HashMap<>();
		respone.put("User deleted: ", Boolean.TRUE);
		return respone;
	};
	
}
