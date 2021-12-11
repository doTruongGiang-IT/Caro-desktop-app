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
    @ManyToOne
    @JoinColumn(name="user_1")
    private User user_1;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_2")
    private User user_2;

    @Column(name = "result")
    private int result;
    
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

    public Match() {
    }

	public Match(@NotNull User user_1, @NotNull User user_2, @NotNull int result, @NotNull int result_type,
			@NotNull @NotBlank(message = "Start date is required") String start_date,
			@NotNull @NotBlank(message = "End date is required") String end_date) {
		super();
		this.user_1 = user_1;
		this.user_1 = user_2;
		this.result = result;
		this.result_type = result_type;
		this.start_date = start_date;
		this.end_date = end_date;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser_1() {
		return user_1;
	}

	public void setUser_1(User user_1) {
		this.user_1 = user_1;
	}

	public User getUser_2() {
		return user_2;
	}

	public void setUser_2(User user_2) {
		this.user_2 = user_2;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
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
	
}
