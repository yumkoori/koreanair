package com.koreanair.model.dto;

public class PaymentVerifyDTO {
    private String impUid;          // 아임포트 결제 고유번호
    private String merchantUid;     // 가맹점 주문번호
    private String status;          // 결제 상태
    private int amount;             // 결제 금액
    private String payMethod;       // 결제 방법
    private String paidAt;          // 결제 완료 시간
    private String failReason;      // 실패 사유
    private String buyerName;       // 구매자 이름
    private String buyerEmail;      // 구매자 이메일
    private String buyerTel;        // 구매자 전화번호

    // 기본 생성자
    public PaymentVerifyDTO() {}

    // 모든 필드를 포함하는 생성자
    public PaymentVerifyDTO(String impUid, String merchantUid, String status, int amount, 
                     String payMethod, String paidAt, String failReason, 
                     String buyerName, String buyerEmail, String buyerTel) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.status = status;
        this.amount = amount;
        this.payMethod = payMethod;
        this.paidAt = paidAt;
        this.failReason = failReason;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerTel = buyerTel;
    }

    // Getter 및 Setter 메서드들
    public String getImpUid() {
        return impUid;
    }

    public void setImpUid(String impUid) {
        this.impUid = impUid;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "impUid='" + impUid + '\'' +
                ", merchantUid='" + merchantUid + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", payMethod='" + payMethod + '\'' +
                ", paidAt='" + paidAt + '\'' +
                ", failReason='" + failReason + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", buyerTel='" + buyerTel + '\'' +
                '}';
    }
}
