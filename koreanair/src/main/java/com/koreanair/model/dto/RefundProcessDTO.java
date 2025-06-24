package com.koreanair.model.dto;

public class RefundProcessDTO {
    private String bookingId;
    private String merchantUid;
    private int userNo;
    private String amount;
    private String refundReason;
    private String refundStatus;
    
    // 기본 생성자
    public RefundProcessDTO() {}
    
    // 매개변수 생성자 - 환불 검증용
    public RefundProcessDTO(String bookingId, int userNo) {
        this.bookingId = bookingId;
        this.userNo = userNo;
    }
    
    // 매개변수 생성자 - 환불 처리용
    public RefundProcessDTO(String bookingId, String merchantUid, int userNo, String amount, String refundReason) {
        this.bookingId = bookingId;
        this.merchantUid = merchantUid;
        this.userNo = userNo;
        this.amount = amount;
        this.refundReason = refundReason;
        this.refundStatus = "REQUESTED";
    }
    
    // Getter와 Setter 메소드들
    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getMerchantUid() {
        return merchantUid;
    }
    
    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }
    
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    public String getAmount() {
        return amount;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    
    public String getRefundStatus() {
        return refundStatus;
    }
    
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
    
    @Override
    public String toString() {
        return "RefundProcessDTO{" +
                "bookingId='" + bookingId + '\'' +
                ", merchantUid='" + merchantUid + '\'' +
                ", userNo=" + userNo +
                ", amount='" + amount + '\'' +
                ", refundReason='" + refundReason + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                '}';
    }
} 