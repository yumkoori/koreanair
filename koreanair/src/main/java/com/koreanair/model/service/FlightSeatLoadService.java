package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;

public class FlightSeatLoadService {
	
	public List<FlightSeatSaveDTO> seatload( String planeType ){
		System.out.println(planeType);
		ProjectDao dao = new ProjectDaoimpl();
		
        try {
            // 3. DAO의 메소드를 '호출'하고 그 결과를 받습니다. *** 이 부분이 누락되었습니다 ***
            //    이제 ProjectDaoimpl의 flightSeatload()가 실행됩니다.
            List<FlightSeatSaveDTO> seatList = dao.flightSeatload(planeType);
            
            // 4. DAO로부터 받은 결과를 반환합니다.
            return seatList;
            
        } catch (Exception e) {
            // 실제 운영 코드에서는 예외 처리를 더 견고하게 해야 합니다.
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
		
		
	}
}
