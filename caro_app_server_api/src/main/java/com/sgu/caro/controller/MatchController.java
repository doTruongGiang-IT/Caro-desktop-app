package com.sgu.caro.controller;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.sgu.caro.entity.Match;
import com.sgu.caro.entity.User;
import com.sgu.caro.exception.ResourceNotFoundException;
import com.sgu.caro.repository.MatchRepository;
import com.sgu.caro.repository.UserRepository;
import com.sgu.caro.token.JwtService;

@RestController
@RequestMapping("/caro_api/")
public class MatchController {
	
	public static final String ADMIN_ROLE = "admin";

	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
    JwtService jwtService;
	
	@GetMapping("matches")
    public List<Object> getAllMatch(@RequestHeader Map<String, String> headers) {
		List<Object> formatMatches = new ArrayList<Object>();
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
        if(flag) {
        	List<Match> matches = matchRepository.findAll();
        	for (Match match : matches) {
        		User user_1 = userRepository.findById(match.getUser_1()).orElseThrow(() -> new ResourceNotFoundException("User 1 not found"));
                User user_2 = userRepository.findById(match.getUser_2()).orElseThrow(() -> new ResourceNotFoundException("User 2 not found"));
                LinkedHashMap<String, Object> hashMatch = new LinkedHashMap<String, Object>();
                
                long id = match.getId();
                long user1 = match.getUser_1();
                String name1 = user_1.getFirstName() + " " + user_1.getLastName();
                long user2 = match.getUser_2();
                String name2 = user_2.getFirstName() + " " + user_2.getLastName();
                long result = match.getResult();
                int result_type = match.getResult_type();
                String startDate = match.getStart_date();
                String endDate = match.getEnd_date();
                
                hashMatch.put("id", id);
                hashMatch.put("user_1", user1);
                hashMatch.put("name_1", name1);
                hashMatch.put("user_2", user2);
                hashMatch.put("name_2", name2);
                hashMatch.put("result", result);
                hashMatch.put("result_type", result_type);
                hashMatch.put("start_date", startDate);
                hashMatch.put("end_date", endDate);
                formatMatches.add(hashMatch);
        	};
        };
        
        return flag ? formatMatches : null;
    };
    
    @GetMapping("matches/{id}")
    public ResponseEntity<Object> getMatchById(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long matchId) {
        Match match = null;
        LinkedHashMap<String, Object> hashMatch = new LinkedHashMap<String, Object>();
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
            match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
            User user_1 = userRepository.findById(match.getUser_1()).orElseThrow(() -> new ResourceNotFoundException("User 1 not found"));
            User user_2 = userRepository.findById(match.getUser_2()).orElseThrow(() -> new ResourceNotFoundException("User 2 not found"));
            
            long id = match.getId();
            long user1 = match.getUser_1();
            String name1 = user_1.getFirstName() + " " + user_1.getLastName();
            long user2 = match.getUser_2();
            String name2 = user_2.getFirstName() + " " + user_2.getLastName();
            long result = match.getResult();
            int result_type = match.getResult_type();
            String startDate = match.getStart_date();
            String endDate = match.getEnd_date();
            
            hashMatch.put("id", id);
            hashMatch.put("user_1", user1);
            hashMatch.put("name_1", name1);
            hashMatch.put("user_2", user2);
            hashMatch.put("name_2", name2);
            hashMatch.put("result", result);
            hashMatch.put("result_type", result_type);
            hashMatch.put("start_date", startDate);
            hashMatch.put("end_date", endDate);
        };
        return flag ? ResponseEntity.ok().body(hashMatch) : null;
    };
    
    @PostMapping("matches")
    public Match createMatch(@Valid @RequestBody Match match) throws Exception {
        String startDate = match.getStart_date();
        String endDate = match.getEnd_date();
        long user1 = match.getUser_1();
        long user2 = match.getUser_2();
        
        User user_1 = userRepository.findById(user1).orElseThrow(() -> new ResourceNotFoundException("User 1 not found"));
        User user_2 = userRepository.findById(user2).orElseThrow(() -> new ResourceNotFoundException("User 2 not found"));
        
    	String regex = "([0-9]{4})/([0-9]{2})/([0-9]{2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})";
        if (!startDate.matches(regex)) {
            match.setStart_date(null);
        };
        if (!endDate.matches(regex)) {
            match.setEnd_date(null);
        };
        if(user1 == user2) {
        	throw new Exception("Wrong format for create match");
        };
        if((match.getResult() != user1 && match.getResult() != user2) || match.getResult_type() >= 4 || match.getResult_type() == 1) {
        	match.setResult(0);
        };
        
        /*0: Thắng bình thường
         * 1: Hòa
         * 2: Người chơi hết lượt
         * 3: Người chơi rời cuộc đấu
        */
        if(match.getResult_type() >= 4 || match.getResult() == 0) {
        	match.setResult_type(1);
        };
        
        // User 1 win
        if(match.getResult() == user1) {
        	Field field = ReflectionUtils.findField(User.class, "score");
        	if(field != null) {
        		field.setAccessible(true);
        		ReflectionUtils.setField(field, user_1, user_1.getScore() + 3);
        		ReflectionUtils.setField(field, user_2, Math.max(user_2.getScore() - 1, 0));
        	};
    		user_1.setScore(user_1.getScore());
    		user_2.setScore(user_2.getScore());
            userRepository.save(user_1);
            userRepository.save(user_2);
        };
        
        // User 2 win
        if(match.getResult() == user2) {
        	Field field = ReflectionUtils.findField(User.class, "score");
        	if(field != null) {
        		field.setAccessible(true);
        		ReflectionUtils.setField(field, user_2, user_2.getScore() + 3);
        		ReflectionUtils.setField(field, user_1, Math.max(user_1.getScore() - 1, 0));
        	};
    		user_2.setScore(user_2.getScore());
    		user_1.setScore(user_1.getScore());
            userRepository.save(user_2);
            userRepository.save(user_1);
        };
        
        // User 1 & 2 even
        if(match.getResult() == 0) {
        	Field field = ReflectionUtils.findField(User.class, "score");
        	if(field != null) {
        		field.setAccessible(true);
        		ReflectionUtils.setField(field, user_2, user_2.getScore() + 1);
        		ReflectionUtils.setField(field, user_1, user_1.getScore() + 1);
        	};
    		user_2.setScore(user_2.getScore());
    		user_1.setScore(user_1.getScore());
            userRepository.save(user_2);
            userRepository.save(user_1);
        };
        
        return matchRepository.save(match);
    };
	
}

//  for(var entry : type.entrySet()) {
//	if(entry.getKey().equals("win")) {
//		ReflectionUtils.setField(field, user, user.getScore() + 3);
//	};
//	if(entry.getKey().equals("lose")) {
//		ReflectionUtils.setField(field, user, user.getScore() - 1);
//	};
//	if(entry.getKey().equals("even")) {
//		ReflectionUtils.setField(field, user, user.getScore() + 1);
//	};
//};
