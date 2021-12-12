package com.sgu.caro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sgu.caro.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
