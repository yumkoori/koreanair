package com.koreanair.model.dao;

import com.koreanair.model.dto.RefundDetailDTO;

public interface RefundDAO {

    /**
     * 예약 번호와 사용자 ID를 기반으로 환불에 필요한 상세 정보를 조회합니다.
     * 이 메소드는 예약 정보와 운임 정보를 조합하여 RefundDetailDTO를 반환합니다.
     * @param bookingId 조회할 예약 번호
     * @param userId 예약 소유주 확인을 위한 사용자 ID
     * @return 환불 상세 정보가 담긴 DTO, 정보가 없으면 null
     */
    public RefundDetailDTO findRefundDetailsByBookingId(String bookingId, String userId);

}