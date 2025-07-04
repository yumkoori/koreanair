package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.AdminReservationDTO;
import com.koreanair.model.dto.ClassPriceSaveDTO;
import com.koreanair.model.dto.DashBoardStatsDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.MonthReservationDTO;
import com.koreanair.model.dto.PopularDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
import com.koreanair.model.dto.SearchUserDTO;
import com.koreanair.model.dto.SeatRevenueDTO;

public interface ProjectDao {
	
	public String fetchFlightData(String apiUrl) throws Exception;
	
	public int seatsave(List<FlightSeatSaveDTO> seatList , String id) throws Exception;
	
	public List<FlightSeatSaveDTO> flightSeatload(String flight_id) throws Exception;
	
	public int searchcarftid( String flight_id) throws Exception;
	
	public boolean checkDuplicateSeat(List<FlightSeatSaveDTO> seatList) throws Exception;

	public int saveSchdulesDB(List<SaveSchedulesDBDTO> scheduleList);

	public boolean refreshCheck(List<SaveSchedulesDBDTO> refresList);
	
	public List<SaveSchedulesDBDTO> refreshSchdules(List<SaveSchedulesDBDTO> refresList) throws Exception;
	
	public int priceSave(List<ClassPriceSaveDTO> priceList, String flightid) throws Exception;
	
	public List<SearchUserDTO> searchUsers(String username) throws Exception;
	
	public List<DashBoardStatsDTO> dashLoad() throws Exception;
	
	public List<SeatRevenueDTO> seatRevenue() throws Exception;
	
	public List<MonthReservationDTO> monthReservation(String year) throws Exception;
	
	public List<PopularDTO> popularroutes( String year ) throws Exception;
	
	
	public List<AdminReservationDTO> reservations( String searchType, String searchKeyword, String status ) throws Exception;
}
