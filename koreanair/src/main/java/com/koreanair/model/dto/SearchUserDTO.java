package com.koreanair.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDTO {
	
	String user_no;
	String grade;
	String user_id;
	String pw;
	String email;
	String ko_name;
	String en_name;
	Date birth_date;
	String gender;
	String address;
	String phone_number;
	Date created_at;
	String status;
}
