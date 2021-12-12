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
        	List<Match> matches = (List<Match>) matchRepository.findAll();
        	User userStats = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        	Predicate<Match> byUserId = user -> (user.getUser_1() == userId || user.getUser_2() == userId);
        	Predicate<Match> byUserWinId = user -> (user.getResult() == userId);
        	List<Match> userMatches = matches.stream().filter(byUserId).collect(Collectors.toList());
        	List<Match> userWinMatches = userMatches.stream().filter(byUserWinId).collect(Collectors.toList());
        	
        	int matchesCount = userMatches.size();
        	int winMatchesCount = userWinMatches.size();
    		float winRate = (winMatchesCount * 100) / matchesCount;
    		
    		hashAchievement.put("win_rate", winRate);
    		hashAchievement.put("win_count", winMatchesCount);
    		hashAchievement.put("lose_count", matchesCount - winMatchesCount);
    		hashAchievement.put("win_length", winMatchesCount);
    		hashAchievement.put("lose_length", matchesCount - winMatchesCount);
    		hashAchievement.put("score", userStats.getScore());
        };
        return flag ? ResponseEntity.ok().body(hashAchievement) : null;
    };
	
}
