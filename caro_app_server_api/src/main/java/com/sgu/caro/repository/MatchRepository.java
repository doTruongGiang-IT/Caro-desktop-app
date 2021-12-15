package com.sgu.caro.repository;

import java.util.ArrayList;

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
	
	public static ArrayList<Integer> quicksort(ArrayList<Integer> list) {
        if (list.size() <= 1) {
            return list;
        }
        ArrayList<Integer> lessThanPivot = new ArrayList<Integer>();
        ArrayList<Integer> greaterThanPivot = new ArrayList<Integer>();
        int pivot = list.get(0);
        int length = list.size();
        for (int i = 1; i < length; i++) {
            int currentValue = list.get(i);
            if (currentValue <= pivot) {
                lessThanPivot.add(currentValue);
            } else {
                greaterThanPivot.add(currentValue);
            }
        }
        ArrayList<Integer> sortedList = new ArrayList<Integer>();
        sortedList.addAll(quicksort(lessThanPivot));
        sortedList.add(pivot);
        sortedList.addAll(quicksort(greaterThanPivot));
        return sortedList;
    };
	
}
