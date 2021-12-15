package com.sgu.caro.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "matches")
public class Match {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name="user_1")
    private long user_1;

    @NotNull
    @Column(name="user_2")
    private long user_2;

    @Column(name = "result")
    private long result;
    
    @NotNull
    @Column(name = "result_type")
    private int result_type;

    @NotNull
    @NotBlank(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    @Column(name = "start_date")
    private String start_date;
    
    @NotNull
    @NotBlank(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    @Column(name = "end_date")
    private String end_date;
    
//    @NotNull
//    @NotBlank(message = "Time play is required")
//    @DateTimeFormat(pattern = "hh:mm:ss")
//    @Column(name = "timePlay")
//    private String timePlay;

    public Match() {
    }

	public Match(@NotNull long user_1, @NotNull long user_2, @NotNull long result, @NotNull int result_type,
			@NotNull @NotBlank(message = "Start date is required") String start_date,
			@NotNull @NotBlank(message = "End date is required") String end_date
//			@NotNull @NotBlank(message = "Time play is required") String timePlay
	) {
		super();
		this.user_1 = user_1;
		this.user_1 = user_2;
		this.result = result;
		this.result_type = result_type;
		this.start_date = start_date;
		this.end_date = end_date;
//		this.timePlay = timePlay;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_1() {
		return user_1;
	}

	public void setUser_1(long user_1) {
		this.user_1 = user_1;
	}

	public long getUser_2() {
		return user_2;
	}

	public void setUser_2(long user_2) {
		this.user_2 = user_2;
	}

	public long getResult() {
		return result;
	}

	public void setResult(long result) {
		this.result = result;
	}

	public int getResult_type() {
		return result_type;
	}

	public void setResult_type(int result_type) {
		this.result_type = result_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

//	public String getTimePlay() {
//		return timePlay;
//	}
//
//	public void setTimePlay(String timePlay) {
//		this.timePlay = timePlay;
//	}
	
}
