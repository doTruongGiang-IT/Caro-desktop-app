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

@RestController
@RequestMapping("/caro_api/")
public class StatsController {

	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
    JwtService jwtService;
	
	@GetMapping("stats/{id}")
    public ResponseEntity<Object> getMatchById(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long userId) {
		LinkedHashMap<String, Object> hashAchievement = new LinkedHashMap<String, Object>();
        boolean flag = false;
        
        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("authorization")) {
                String username = jwtService.getUsernameFromToken(entry.getValue());
                if (!jwtService.isTokenExpired(entry.getValue())) {
                    flag = true;
                };
            };
        };
        if(flag) {
        	int winLength = 0;
        	int loseLength = 0;
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
    		
//    		for(int i = 0; i < matchesCount; i++) {
//    			int winTemp = 0;
//				int loseTemp = 0;
//				
//    			for(int j = i+1; j < matchesCount; j++) {
//    				if(userMatches.get(i).getResult() == userId && userMatches.get(j).getResult() == userId) {
//        				winTemp++;
//        			};
//        			
//        			if((userMatches.get(i).getResult() != userId && userMatches.get(j).getResult() != userId) && (userMatches.get(i).getResult() != 0 && userMatches.get(j).getResult() != 0)) {
//        				loseTemp++;
//        			};
//    			};
//    			
//    			if(winLength <= winTemp) {
//    				winLength = winTemp;
//    			};
//    			
//    			if(loseLength <= loseTemp) {
//    				loseLength = loseTemp;
//    			};
//    		};
//    		
//    		if(winMatchesCount == 1) {
//    			winLength = 1;
//    		};
//    		if(loseMatchesCount == 1) {
//    			loseLength = 1;
//    		};
    		
    		hashAchievement.put("win_rate", winRate);
    		hashAchievement.put("win_count", winMatchesCount);
    		hashAchievement.put("lose_count", loseMatchesCount);
    		hashAchievement.put("win_length", winMatchesCount);
    		hashAchievement.put("lose_length", loseMatchesCount);
    		hashAchievement.put("score", userStats.getScore());
        };
        return flag ? ResponseEntity.ok().body(hashAchievement) : null;
    };
	
}
