package com.sgu.caro.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	@Email
	@Column(name = "username")
	private String username;
	
	@NotNull
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password should have at least 6 characters")
	@Column(name = "password")
	private String password;
	
	@NotNull
	@NotBlank(message = "First name is required")
	@Size(min = 2, message = "First name should have at least 2 characters")
	@Column(name = "first_name")
	private String firstName;
	
	@NotNull
	@NotBlank(message = "Last name is required")
	@Size(min = 3, message = "Last name should have at least 3 characters")
	@Column(name = "last_name")
	private String lastName;
	
	@NotNull
	@NotBlank(message = "Gender is required")
	@Column(name = "gender")
	private String gender;
	
	@NotNull
	@NotBlank(message = "Day_of_birth is required")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "day_of_birth")
	private String dayOfBirth;
	
	public User() {};

	public User(@NotNull @NotEmpty @Email String username,
			@NotNull @NotBlank(message = "Password is required") @Size(min = 6, message = "Password should have at least 6 characters") String password,
			@NotNull @NotBlank(message = "First name is required") @Size(min = 2, message = "First name should have at least 2 characters") String firstName,
			@NotNull @NotBlank(message = "Last name is required") @Size(min = 3, message = "Last name should have at least 3 characters") String lastName,
			@NotNull @NotBlank(message = "Gender is required") String gender,
			@NotNull @NotBlank(message = "Day_of_birth is required") String dayOfBirth) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dayOfBirth = dayOfBirth;
	};

	public long getId() {
		return id;
	};

	public void setId(long id) {
		this.id = id;
	};

	public String getUsername() {
		return username;
	};

	public void setUsername(String username) {
		this.username = username;
	};

	public String getPassword() {
		return password;
	};

	public void setPassword(String password) {
		this.password = password;
	};

	public String getFirstName() {
		return firstName;
	};

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	};

	public String getLastName() {
		return lastName;
	};

	public void setLastName(String lastName) {
		this.lastName = lastName;
	};

	public String getGender() {
		return gender;
	};

	public void setGender(String gender) {
		this.gender = gender;
	};

	public String getDayOfBirth() {
		return dayOfBirth;
	};

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	};

}
