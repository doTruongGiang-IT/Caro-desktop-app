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
    public List<Match> getAllMatch(@RequestHeader Map<String, String> headers) {
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
        return flag ? this.matchRepository.findAll() : null;
    };
    
    @GetMapping("matches/{id}")
    public ResponseEntity<Match> getMatchById(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") long matchId) {
        Match match = null;
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
        };
        return flag ? ResponseEntity.ok().body(match) : null;
    };
    
    @PostMapping("matches")
    public Match createMatch(@Valid @RequestBody Match match) {
        String startDate = match.getStart_date();
        String endDate = match.getEnd_date();
        User user1 = match.getUser_1();
        User user2 = match.getUser_2();
    	String regex = "([0-9]{4})/([0-9]{2})/([0-9]{2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})";
        if (!startDate.matches(regex)) {
            match.setStart_date(null);
        };
        if (!endDate.matches(regex)) {
            match.setEnd_date(null);
        };
        if(user1.getId() == user2.getId()) {
        	match.setUser_1(null);
        	match.setUser_2(null);
        };
        if(match.getResult() != user1.getId() && match.getResult() != user2.getId()) {
        	match.setResult(0);
        };
        
        /*0: Thắng bình thường
         * 1: Hòa
         * 2: Người chơi hết lượt
         * 3: Người chơi rời cuộc đấu
        */
        if(match.getResult_type() >= 4) {
        	match.setResult_type(1);
        };
        return matchRepository.save(match);
    };
	
}
