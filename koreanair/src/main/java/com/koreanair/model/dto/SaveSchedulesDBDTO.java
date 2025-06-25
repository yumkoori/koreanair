package com.koreanair.model.dto;

import java.time.LocalTime;

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
public class SaveSchedulesDBDTO {
	String id;
	String flightNo;
	String origin;
	String destination;
	String departureTime;
	String arrivalTime;
	String status;
	String airline;
	
}
