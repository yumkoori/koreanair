package com.koreanair.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 환불 신청 페이지에 필요한 모든 데이터를 담는 DTO 클래스.
 * 기존 예약 정보와 계산된 환불 관련 금액 정보를 포함합니다.
 * Lombok을 사용하여 Getter, Setter, 생성자 등의 코드를 자동 생성합니다.
 */
@Data               // @Getter, @Setter, @ToString, @EqualsAndHashCode 등을 모두 포함
@NoArgsConstructor  // 파라미터가 없는 기본 생성자
@AllArgsConstructor // 모든 필드를 포함하는 생성자
public class RefundDetailDTO {

    // 기존 예약 정보를 담는 객체
    private ReservationDTO reservation;

    // 환불 계산 관련 필드
    private double baseFare;        // 기본 운임 (마일 또는 원)
    private double tax;             // 유류할증료 및 세금
    private int penaltyFee;         // 환불 수수료 및 위약금
    private double totalRefundAmount; // 최종 환불 예상 금액 (원)
    private double totalRefundMileage; // 최종 환불 예상 마일리지

}