package com.sgu.caro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
		return userRepository.save(user);
	};
	
	// Update smart phone by id
	//@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") long userId, @Valid @RequestBody User updateUser) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		user.setUsername(updateUser.getUsername());
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setGender(updateUser.getGender());
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
