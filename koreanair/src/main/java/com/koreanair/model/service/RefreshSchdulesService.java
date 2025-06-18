package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.FlightSeatSaveDTO;
import com.koreanair.model.dto.SaveSchedulesDBDTO;

public class RefreshSchdulesService {
	
	public List<SaveSchedulesDBDTO> refreshschdules( List<SaveSchedulesDBDTO> refresList ){
<<<<<<< HEAD
		// System.out.println("서비스에서 받는 >  " + refresList);
=======
<<<<<<< HEAD
		// System.out.println("서비스에서 받는 >  " + refresList);
=======
		System.out.println("서비스에서 받는 >  " + refresList);
>>>>>>> 1f3e8f056f7dad1b16f9666de5ce634c38e3b706
>>>>>>> cd5ba6535013433d0eef20955581fa8717c00dbc
		ProjectDao dao = new ProjectDaoimpl();
		
        try {
            List<SaveSchedulesDBDTO> refreshSchdules = dao.refreshSchdules(refresList);
            
            // 4. DAO로부터 받은 결과를 반환합니다.
            return refreshSchdules;
            
        } catch (Exception e) {
            // 실제 운영 코드에서는 예외 처리를 더 견고하게 해야 합니다.
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
		
		
	}

}
