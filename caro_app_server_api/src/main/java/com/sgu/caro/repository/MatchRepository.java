package com.sgu.caro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sgu.caro.entity.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

	@Query("SELECT m FROM Match m WHERE m.user_1=:userId or m.user_2=:userId")
	Match findUserMatches(@Param("userId") long id);
	
	@Query("SELECT m FROM Match m WHERE m.result=:userId")
	Match findUserWinMatches(@Param("userId") long id);
	
}
