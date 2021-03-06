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

    @GetMapping("rating/shortest_matches")
    public List<Object> getTenShortestMatchByTimePlay(@RequestHeader Map<String, String> headers) {
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
        //Sort.by(Sort.Direction.ASC, "timePlay")
        if (flag) {
            List<Match> matches = new ArrayList<Match>();
            matches = matchRepository.findAll();
            String startDate = "";
            String endDate = "";

            for (int i = 0; i < matches.size() - 1; i++) {
                Match temp = matches.get(0);
                for (int j = i + 1; j < matches.size(); j++) {
                    long timeI;
                    try {
                        timeI = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(i).getEnd_date()).getTime() - new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(i).getStart_date()).getTime();
                        long timeJ = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(j).getEnd_date()).getTime() - new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(j).getStart_date()).getTime();
                        if (timeI > timeJ) {
                            temp = matches.get(j);
                            matches.set(j, matches.get(i));
                            matches.set(i, temp);
                        };
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
                };
            };

            for (Match match : matches) {
                LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
                long id = match.getId();
                long user1 = match.getUser_1();
                long user2 = match.getUser_2();
                long result = match.getResult();
                int result_type = match.getResult_type();
                startDate = match.getStart_date();
                endDate = match.getEnd_date();
                Date dateStart;
                Date dateEnd;
                String timePlay = "";
                try {
                    dateStart = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(startDate);
                    dateEnd = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(endDate);
                    long miliseconds = dateEnd.getTime() - dateStart.getTime();
                    String minute = String.valueOf((miliseconds / 1000) / 60);
                    String second = String.valueOf((miliseconds / 1000) % 60);
                    timePlay = minute + ":" + second;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };

                User player1 = userRepository.findById(user1).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                User player2 = userRepository.findById(user2).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                hashStats.put("id", id);
                hashStats.put("user_1", player1.getFirstName() + " " + player1.getLastName());
                hashStats.put("user_2", player2.getFirstName() + " " + player2.getLastName());
                hashStats.put("user_win", result == user1 ? player1.getFirstName() + " " + player1.getLastName() : (result == user2 ? player2.getFirstName() + " " + player2.getLastName() : "Tr???n h??a"));
                hashStats.put("start_date", startDate);
                hashStats.put("end_date", endDate);
                hashStats.put("time_play", timePlay);
                stats.add(hashStats);
            };

        };

        return flag ? stats : null;
    }

    ;
    
    @GetMapping("rating/longest_matches")
    public List<Object> getTenLongestMatchByTimePlay(@RequestHeader Map<String, String> headers) {
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
        //Sort.by(Sort.Direction.ASC, "timePlay")
        if (flag) {
            List<Match> matches = new ArrayList<Match>();
            matches = matchRepository.findAll();
            String startDate = "";
            String endDate = "";

            for (int i = 0; i < matches.size() - 1; i++) {
                Match temp = matches.get(0);
                for (int j = i + 1; j < matches.size(); j++) {
                    long timeI;
                    try {
                        timeI = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(i).getEnd_date()).getTime() - new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(i).getStart_date()).getTime();
                        long timeJ = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(j).getEnd_date()).getTime() - new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(matches.get(j).getStart_date()).getTime();
                        if (timeI < timeJ) {
                            temp = matches.get(j);
                            matches.set(j, matches.get(i));
                            matches.set(i, temp);
                        };
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
                };
            };

            for (Match match : matches) {
                LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
                long id = match.getId();
                long user1 = match.getUser_1();
                long user2 = match.getUser_2();
                long result = match.getResult();
                int result_type = match.getResult_type();
                startDate = match.getStart_date();
                endDate = match.getEnd_date();
                Date dateStart;
                Date dateEnd;
                String timePlay = "";
                try {
                    dateStart = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(startDate);
                    dateEnd = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(endDate);
                    long miliseconds = dateEnd.getTime() - dateStart.getTime();
                    String minute = String.valueOf((miliseconds / 1000) / 60);
                    String second = String.valueOf((miliseconds / 1000) % 60);
                    timePlay = minute + ":" + second;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };

                User player1 = userRepository.findById(user1).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                User player2 = userRepository.findById(user2).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                hashStats.put("id", id);
                hashStats.put("user_1", player1.getFirstName() + " " + player1.getLastName());
                hashStats.put("user_2", player2.getFirstName() + " " + player2.getLastName());
                hashStats.put("user_win", result == user1 ? player1.getFirstName() + " " + player1.getLastName() : (result == user2 ? player2.getFirstName() + " " + player2.getLastName() : "Tr???n h??a"));
                hashStats.put("start_date", startDate);
                hashStats.put("end_date", endDate);
                hashStats.put("time_play", timePlay);
                stats.add(hashStats);
            };

        };

        return flag ? stats : null;
    }

    ;
	
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
        if (flag) {
            List<User> users = new ArrayList<User>();
            users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
            for (User user : users) {
                LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
                long id = user.getId();
                String username = user.getUsername();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                double score = user.getScore();
                float winRate = user.getWin_rate();
                int winLength = user.getWin_length();

                hashStats.put("id", id);
                hashStats.put("username", username);
                hashStats.put("name", firstName + " " + lastName);
                hashStats.put("win_rate", winRate);
                hashStats.put("win_length", winLength);
                hashStats.put("score", score);
                stats.add(hashStats);
            };
        };

        return flag ? stats : null;
    }

    ;
    
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
        if (flag) {
            List<User> users = new ArrayList<User>();
            users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "winRate"));
            for (User user : users) {
                LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
                long id = user.getId();
                String username = user.getUsername();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                double score = user.getScore();
                float winRate = user.getWin_rate();
                int winLength = user.getWin_length();

                hashStats.put("id", id);
                hashStats.put("username", username);
                hashStats.put("name", firstName + " " + lastName);
                hashStats.put("win_rate", winRate);
                hashStats.put("win_length", winLength);
                hashStats.put("score", score);
                stats.add(hashStats);
            };
        };

        return flag ? stats : null;
    }

    ;
    
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
        if (flag) {
            List<User> users = new ArrayList<User>();
            users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "winLength"));
            for (User user : users) {
                LinkedHashMap<String, Object> hashStats = new LinkedHashMap<String, Object>();
                long id = user.getId();
                String username = user.getUsername();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                double score = user.getScore();
                float winRate = user.getWin_rate();
                int winLength = user.getWin_length();

                hashStats.put("id", id);
                hashStats.put("username", username);
                hashStats.put("name", firstName + " " + lastName);
                hashStats.put("win_rate", winRate);
                hashStats.put("win_length", winLength);
                hashStats.put("score", score);
                stats.add(hashStats);
            };
        };

        return flag ? stats : null;
    }

    ;
	
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
        if (flag) {
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
            float winRate = 0f;

            if (matchesCount > 0) {
                winRate = (winMatchesCount * 100) / matchesCount;
            };

            Comparator<Match> compareById = (Match match1, Match match2) -> match1.getStart_date().compareTo(match2.getStart_date());
            Collections.sort(userMatches, compareById);

            int win_length = 1;
            int loss_length = 1;

            for (int i = 1; i < matchesCount; i++) {
                if (userMatches.get(i).getResult() == userId) {
                    if (userMatches.get(i - 1).getResult() == userMatches.get(i).getResult()) {
                        win_length++;
                    } else {
                        win_length = 1;
                    };
                } else if (userMatches.get(i).getResult() != userId && userMatches.get(i).getResult() != 0) {
                    if (userMatches.get(i - 1).getResult() != userId && userMatches.get(i - 1).getResult() != 0) {
                        loss_length++;
                    } else {
                        loss_length = 1;
                    };
                };
                max_win_length = Math.max(max_win_length, win_length);
                max_loss_length = Math.max(max_loss_length, loss_length);
            };

            hashAchievement.put("win_rate", winRate);
            hashAchievement.put("win_count", winMatchesCount == 0 ? 0 : winMatchesCount);
            hashAchievement.put("lose_count", loseMatchesCount == 0 ? 0 : loseMatchesCount);
            hashAchievement.put("win_length", winMatchesCount == 0 ? 0 : max_win_length);
            hashAchievement.put("lose_length", loseMatchesCount == 0 ? 0 : max_loss_length);
            hashAchievement.put("score", userStats.getScore());

            userStats.setWin_rate(winRate);
            userStats.setWin_length(max_win_length);

            Field win_rate = ReflectionUtils.findField(User.class, "win_rate");
            Field winLength = ReflectionUtils.findField(User.class, "win_length");
            if (win_rate != null) {
                win_rate.setAccessible(true);
                ReflectionUtils.setField(win_rate, userStats, winRate);
            };
            if (winLength != null) {
                winLength.setAccessible(true);
                if (winMatchesCount == 0) {
                    ReflectionUtils.setField(winLength, userStats, 0);
                } else {
                    ReflectionUtils.setField(winLength, userStats, max_win_length);
                }
            };
            userRepository.save(userStats);
        };
        return flag ? ResponseEntity.ok().body(hashAchievement) : null;
    }
;

}
