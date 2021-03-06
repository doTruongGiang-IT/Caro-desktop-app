package com.sgu.caro.controller;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sgu.caro.entity.User;
import com.sgu.caro.exception.ResourceNotFoundException;
import com.sgu.caro.repository.UserRepository;
import com.sgu.caro.token.JwtService;
import antlr.Token;

@RestController
@RequestMapping("/caro_api/")
public class UserController {

    public static final String ADMIN_ROLE = "admin";

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("users")
    public List<User> getAllUsers(@RequestHeader Map<String, String> headers) {
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                String role = userRepository.findByUsername(username).getRole();
                if (!jwtService.isTokenExpired(entry.getValue()) || role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        return flag ? this.userRepository.findAll() : null;
    };
	
	@GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
        User user = null;
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                String role = userRepository.findByUsername(username).getRole();
                if (!jwtService.isTokenExpired(entry.getValue()) || role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        if (flag) {
            user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        };
        return flag ? ResponseEntity.ok().body(user) : null;
    };
	
	@PostMapping("auth")
    public ResponseEntity<HashMap<String, String>> authentication(@Valid @RequestBody HashMap<String, String> credential) {
        String username = "";
        String password = "";
        HashMap<String, String> result = new HashMap<>();

        for (String element : credential.keySet()) {
            if (element.equals("username")) {
                username = credential.get(element);
            };
            if (element.equals("password")) {
                password = credential.get(element);
            };
        };

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            if (pbkdf2PasswordEncoder.matches(password, user.getPassword()) && user.isActive()) {
                String token = jwtService.generateTokenLogin(user.getUsername());
                result.put("access_token", token);
                result.put("user_id", String.valueOf(user.getId()));
                result.put("display_name", user.getName());
                result.put("score", String.valueOf(user.getScore()));
            };
            
            if(pbkdf2PasswordEncoder.matches(password, user.getPassword()) && !user.isActive()) {
            	result.put("error", "User has been blocked");
            };
            
            if(!pbkdf2PasswordEncoder.matches(password, user.getPassword())) {
                result.put("error", "Wrong credentials");
            };
        } else {
            throw new UsernameNotFoundException("User not exist");
        };

        return ResponseEntity.ok().body(result);
    };
	
	@PostMapping("authAdmin")
    public ResponseEntity<HashMap<String, String>> authenticationForAdmin(@Valid @RequestBody HashMap<String, String> credential) {
        String username = "";
        String password = "";
        HashMap<String, String> result = new HashMap<String, String>();

        for (String element : credential.keySet()) {
            if (element.equals("username")) {
                username = credential.get(element);
            };
            if (element.equals("password")) {
                password = credential.get(element);
            };
        };

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            if (pbkdf2PasswordEncoder.matches(password, user.getPassword())) {
                String token = jwtService.generateTokenLogin(user.getUsername());
                result.put("access_token", token);
            } else {
                result.put("error", "Wrong credentials");
            };
        } else {
            throw new UsernameNotFoundException("User not exist");
        };

        return ResponseEntity.ok().body(result);
    };
	
	@PostMapping("users")
    public User createUser(@Valid @RequestBody User user) {
        String dateString = user.getDayOfBirth();
        String gender = user.getGender();
        String pass = user.getPassword();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        String regex = "([0-9]{4})/([0-9]{2})/([0-9]{2})";
        String dateRegex = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))/02/29)$" 
        	      + "|^(((19|2[0-9])[0-9]{2})/02/(0[1-9]|1[0-9]|2[0-8]))$"
        	      + "|^(((19|2[0-9])[0-9]{2})/(0[13578]|10|12)/(0[1-9]|[12][0-9]|3[01]))$" 
        	      + "|^(((19|2[0-9])[0-9]{2})/(0[469]|11)/(0[1-9]|[12][0-9]|30))$";
        if (!dateString.matches(dateRegex)) {
            user.setDayOfBirth(null);
        };
        if (!gender.equals("male") && !gender.equals("female") && !gender.equals("undefiend")) {
            user.setGender("undefiend");
        };
        String hashPass = pbkdf2PasswordEncoder.encode(pass);
        user.setPassword(hashPass);
        user.setScore(0);
        user.setRole("user");
        user.setActive(true);
        return userRepository.save(user);
    };
	
	@PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId, @Valid @RequestBody User updateUser) {
        User editUser = null;
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                User user = userRepository.findByUsername(username);
                if (user == null){
                    break;
                }
                String role = userRepository.findByUsername(username).getRole();
                if (!jwtService.isTokenExpired(entry.getValue()) || role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        if (flag) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            String gender = updateUser.getGender();
            String dateString = updateUser.getDayOfBirth();
            String dateRegex = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))/02/29)$" 
          	      + "|^(((19|2[0-9])[0-9]{2})/02/(0[1-9]|1[0-9]|2[0-8]))$"
          	      + "|^(((19|2[0-9])[0-9]{2})/(0[13578]|10|12)/(0[1-9]|[12][0-9]|3[01]))$" 
          	      + "|^(((19|2[0-9])[0-9]{2})/(0[469]|11)/(0[1-9]|[12][0-9]|30))$";
	        if (!dateString.matches(dateRegex)) {
	            user.setDayOfBirth(null);
	        };
	        if (dateString.matches(dateRegex)) {
	            user.setDayOfBirth(dateString);
	        };
            if (!gender.equals("male") && !gender.equals("female") && !gender.equals("undefiend")) {
                user.setGender("undefiend");
            }else {;
            	user.setGender(gender);
            };
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            editUser = userRepository.save(user);
        };
        return flag ? ResponseEntity.ok().body(editUser) : null;
    };
    
//	@PutMapping("block_user/{id}")
//    public ResponseEntity<User> blockUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId, @Valid @RequestBody User updateUser) {
//        User editUser = null;
//        boolean flag = false;
//        for (var entry : headers.entrySet()) {
//            if (entry.getKey().equals("authorization")) {
//                String username = jwtService.getUsernameFromToken(entry.getValue());
//                String role = userRepository.findByUsername(username).getRole();
//                if (role.equals(ADMIN_ROLE)) {
//                    flag = true;
//                };
//            };
//        };
//        if (flag) {
//            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//			user.setActive(updateUser.isActive());
//            editUser = userRepository.save(user);
//        };
//        return flag ? ResponseEntity.ok().body(editUser) : null;
//    };
    
    @PatchMapping("block_user/{id}")
    public ResponseEntity<User> blockUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
        User editUser = null;
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                String role = userRepository.findByUsername(username).getRole();
                if (role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        if (flag) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Field field = ReflectionUtils.findField(User.class, "isActive");
        	if(field != null) {
        		field.setAccessible(true);
            	ReflectionUtils.setField(field, user, false);
        	};
			user.setActive(user.isActive());
            editUser = userRepository.save(user);
        };
        return flag ? ResponseEntity.ok().body(editUser) : null;
    };
    
    @PatchMapping("unblock_user/{id}")
    public ResponseEntity<User> unBlockUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
        User editUser = null;
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                String role = userRepository.findByUsername(username).getRole();
                if (role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        if (flag) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Field field = ReflectionUtils.findField(User.class, "isActive");
        	if(field != null) {
        		field.setAccessible(true);
            	ReflectionUtils.setField(field, user, true);
        	};
			user.setActive(user.isActive());
            editUser = userRepository.save(user);
        };
        return flag ? ResponseEntity.ok().body(editUser) : null;
    };
    
    @PatchMapping("reject_invite/{id}")
    public ResponseEntity<User> score(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
        User editUser = null;
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
//                String username = jwtService.getUsernameFromToken(entry.getValue());
//                String role = userRepository.findByUsername(username).getRole();
                if (!jwtService.isTokenExpired(entry.getValue())) {
                    flag = true;
                };
            };
        };
        if (flag) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Field field = ReflectionUtils.findField(User.class, "score");
        	if(field != null) {
        		field.setAccessible(true);
            	ReflectionUtils.setField(field, user, Math.max(user.getScore() - 1, 0));
        	};
            editUser = userRepository.save(user);
        };
        return flag ? ResponseEntity.ok().body(editUser) : null;
    };
	
	@DeleteMapping("users/{id}")
    public Map<String, Boolean> deleteUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
        Map<String, Boolean> respone = new HashMap<>();
        boolean flag = false;
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                String role = userRepository.findByUsername(username).getRole();
                if (role.equals(ADMIN_ROLE)) {
                    flag = true;
                };
            };
        };
        if (flag) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            this.userRepository.delete(user);
            respone.put("User deleted: ", Boolean.TRUE);
        };
        return flag ? respone : null;
    };

}
