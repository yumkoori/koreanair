package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.ClassPriceSaveDTO;
import com.koreanair.model.dto.DashBoardStatsDTO;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;
<<<<<<< HEAD
import com.koreanair.model.dto.SearchUserDTO;
=======
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706

public interface ProjectDao {
	
	public String fetchFlightData(String apiUrl) throws Exception;
	
	public int seatsave(List<FlightSeatSaveDTO> seatList , String id) throws Exception;
	
	public List<FlightSeatSaveDTO> flightSeatload(String flight_id) throws Exception;
	
	public int searchcarftid( String flight_id) throws Exception;
	
	public boolean checkDuplicateSeat(List<FlightSeatSaveDTO> seatList) throws Exception;

	public int saveSchdulesDB(List<SaveSchedulesDBDTO> scheduleList);

	public boolean refreshCheck(List<SaveSchedulesDBDTO> refresList);
	
	public List<SaveSchedulesDBDTO> refreshSchdules(List<SaveSchedulesDBDTO> refresList) throws Exception;
<<<<<<< HEAD
	
	public int priceSave(List<ClassPriceSaveDTO> priceList, String flightid) throws Exception;
	
	public List<SearchUserDTO> searchUsers(String username) throws Exception;
	
	public List<DashBoardStatsDTO> dashLoad() throws Exception;
=======
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
}
