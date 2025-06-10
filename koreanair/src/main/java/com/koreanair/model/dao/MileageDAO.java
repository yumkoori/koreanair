package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.MileageHistoryDTO;

public interface MileageDAO {
    // 회원의 마일리지 내역 조회
	List<MileageHistoryDTO> getMileageEarnedHistory(String memberNo);

	// 회원의 현재 마일리지 잔액 조회
	int getCurrentMileageBalance(String memberNo);
	
	// 마일리지 적립 
    void addMileage(String membeNo, int amount);
    
    // 마일리지 사용 (보너스 항공권 등 사용)
    void deductMileage(String memberNo, int amount);
    
    
}
