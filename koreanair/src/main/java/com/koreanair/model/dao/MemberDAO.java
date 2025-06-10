package com.koreanair.model.dao;

import com.koreanair.model.dto.MemberDTO;

public interface MemberDAO {

	// 회원 등록
	void insertMember(MemberDTO member);
	
	 // 회원 정보 조회 (id 기반 조회)
	MemberDTO findMemberByNo(String memberNo);
	
	 // 회원 정보 수정
	void updateMemberInfo(MemberDTO member);
	
	 // 회원 탈퇴 처리
	void deactivateMember(String memberId);
	
}
