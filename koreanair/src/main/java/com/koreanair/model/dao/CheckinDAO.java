package com.koreanair.model.dao;

import com.koreanair.model.dto.CheckinDTO;

public interface CheckinDAO {
    boolean createCheckin(CheckinDTO checkin);

    /**
     * [추가] 예약 번호(bookingId)를 기반으로 check_in 테이블의 레코드를 삭제합니다.
     * @param bookingId 삭제할 예약의 ID
     * @return 삭제 성공 시 true
     */
    boolean deleteCheckin(String bookingId);
}