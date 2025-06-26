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
public class DashBoardStatsDTO {
	
	int totalCount;
	int maleCount;
	int fmaleCount;
	int reservations;
}
