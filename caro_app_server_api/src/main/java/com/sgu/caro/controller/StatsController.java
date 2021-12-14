package com.sgu.caro.controller;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/caro_api/")
public class StatsController {
	
	public static final String ADMIN_ROLE = "admin";

	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
    JwtService jwtService;
	
	@GetMapping("rating/score")
    public List<Object> getAllMatchSortByScore(@RequestHeader Map<String, String> headers) {
		List<Object> stats = new ArrayList<Object>();
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
        	List<User> users = new ArrayList<User>();
        	users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
        	for(User user : users) {
        		LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
        		long id = user.getId();
        		String firstName = user.getFirstName();
        		String lastName = user.getLastName();
        		double score = user.getScore();
        		float winRate = user.getWin_rate();
        		int winLength = user.getWin_length();
        		
        		hashStats.put("id", id);
        		hashStats.put("name", firstName + " " + lastName);
        		hashStats.put("win_rate", winRate);
        		hashStats.put("win_length", winLength);
        		hashStats.put("score", score);
        		stats.add(hashStats);
        	};
        };
        
        return flag ? stats : null;
    };
    
    @GetMapping("rating/win_rate")
    public List<Object> getAllMatchSortByWinRate(@RequestHeader Map<String, String> headers) {
		List<Object> stats = new ArrayList<Object>();
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
        	List<User> users = new ArrayList<User>();
        	users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "winRate"));
        	for(User user : users) {
        		LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
        		long id = user.getId();
        		String firstName = user.getFirstName();
        		String lastName = user.getLastName();
        		double score = user.getScore();
        		float winRate = user.getWin_rate();
        		int winLength = user.getWin_length();
        		
        		hashStats.put("id", id);
        		hashStats.put("name", firstName + " " + lastName);
        		hashStats.put("win_rate", winRate);
        		hashStats.put("win_length", winLength);
        		hashStats.put("score", score);
        		stats.add(hashStats);
        	};
        };
        
        return flag ? stats : null;
    };
    
    @GetMapping("rating/win_length")
    public List<Object> getAllMatchSortByWinLength(@RequestHeader Map<String, String> headers) {
		List<Object> stats = new ArrayList<Object>();
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
        	List<User> users = new ArrayList<User>();
        	users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "winLength"));
        	for(User user : users) {
        		LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
        		long id = user.getId();
        		String firstName = user.getFirstName();
        		String lastName = user.getLastName();
        		double score = user.getScore();
        		float winRate = user.getWin_rate();
        		int winLength = user.getWin_length();
        		
        		hashStats.put("id", id);
        		hashStats.put("name", firstName + " " + lastName);
        		hashStats.put("win_rate", winRate);
        		hashStats.put("win_length", winLength);
        		hashStats.put("score", score);
        		stats.add(hashStats);
        	};
        };
        
        return flag ? stats : null;
    };
	
	@GetMapping("stats/{id}")
    public ResponseEntity<Object> getMatchById(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
		LinkedHashMap<String, Object> hashAchievement = new LinkedHashMap<String, Object>();
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
        	int max_win_length = 0;
        	int max_loss_length = 0;
        	List<Match> matches = (List<Match>) matchRepository.findAll();
        	User userStats = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        	Predicate<Match> byUserId = user -> (user.getUser_1() == userId || user.getUser_2() == userId);
        	Predicate<Match> byUserWinId = user -> (user.getResult() == userId);
        	Predicate<Match> byUserLoseId = user -> (user.getResult() != userId && user.getResult() != 0 && (user.getUser_1() == userId || user.getUser_2() == userId));
        	List<Match> userMatches = matches.stream().filter(byUserId).collect(Collectors.toList());
        	List<Match> userWinMatches = userMatches.stream().filter(byUserWinId).collect(Collectors.toList());
        	List<Match> userLoseMatches = userMatches.stream().filter(byUserLoseId).collect(Collectors.toList());
        	
        	int matchesCount = userMatches.size();
        	int winMatchesCount = userWinMatches.size();
        	int loseMatchesCount = userLoseMatches.size();
        	float winRate = (winMatchesCount * 100) / matchesCount;
    		
    		Comparator<Match> compareById = (Match match1, Match match2) -> match1.getStart_date().compareTo(match2.getStart_date());
    		Collections.sort(userMatches, compareById);
    		
    		int win_length = 1;
		    int loss_length = 1;
		    
    		for(int i = 1; i < matchesCount; i++){ 
    		    if(userMatches.get(i).getResult() == userId){
    		        if(userMatches.get(i-1).getResult() == userMatches.get(i).getResult()){
    		            win_length++;
    		        }else{
    		            win_length = 1;
    		        };
    		    }else if (userMatches.get(i).getResult() != userId && userMatches.get(i).getResult() != 0){
    		        if (userMatches.get(i-1).getResult() != userId && userMatches.get(i-1).getResult() != 0){
    		            loss_length++;
    		        }else{
    		            loss_length = 1;
    		        };
    		    };
    		    max_win_length = Math.max(max_win_length, win_length);
    		    max_loss_length = Math.max(max_loss_length, loss_length);
    		};
    		
    		hashAchievement.put("win_rate", winRate);
    		hashAchievement.put("win_count", winMatchesCount);
    		hashAchievement.put("lose_count", loseMatchesCount);
    		hashAchievement.put("win_length", max_win_length);
    		hashAchievement.put("lose_length", max_loss_length);
    		hashAchievement.put("score", userStats.getScore());
    		
    		userStats.setWin_rate(winRate);
    		userStats.setWin_length(max_win_length);
    		
    		Field win_rate = ReflectionUtils.findField(User.class, "win_rate");
    		Field winLength = ReflectionUtils.findField(User.class, "win_length");
        	if(win_rate != null) {
        		win_rate.setAccessible(true);
            	ReflectionUtils.setField(win_rate, userStats, winRate);
        	};
        	if(winLength != null) {
        		winLength.setAccessible(true);
            	ReflectionUtils.setField(winLength, userStats, max_win_length);
        	};
            userRepository.save(userStats);
        };
        return flag ? ResponseEntity.ok().body(hashAchievement) : null;
    };
	
}
