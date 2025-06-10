package com.koreanair.model.dao;

import java.util.List;

import com.koreanair.model.dto.AdminAccountDTO;

public interface AdminDAO {

    // 관리자 계정 생성
    void insertAdminAccount(AdminAccountDTO adminAccount);
    
    // 관리자 계정 수정 (정보 수정, 비밀번호 변경 등)
    void updateAdminAccount(AdminAccountDTO adminAccount);
    
    // 관리자 계정 삭제
    void deleteAdminAccount(String adminId);
    
    // 관리자 계정 상세 조회
    AdminAccountDTO getAdminAccountById(String adminId);
    
   // 관리자 계정 목록 조회 (검색/페이징 지원 가능)
    List<AdminAccountDTO> getAdminAccounts(String searchKeyword, int offset, int limit);

    
}

