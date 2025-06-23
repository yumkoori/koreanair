package com.koreanair.model.dto;

/**
 * 결제 검증을 위한 간단한 DTO
 * 아임포트 API 결과와 DB 저장값을 비교하기 위해 사용
 */
public class PaymentCompareDTO {
    private String merchantUid;  // 가맹점 주문번호
    private int amount;          // 결제 금액
    private String payMethod;    // 결제 방법
    
    // 기본 생성자
    public PaymentCompareDTO() {}
    
    // 매개변수 생성자
    public PaymentCompareDTO(String merchantUid, int amount, String payMethod) {
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.payMethod = payMethod;
    }
    
    // Getter, Setter
    public String getMerchantUid() {
        return merchantUid;
    }
    
    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
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
    
    @Override
    public String toString() {
        return "PaymentCompareDTO{" +
                "merchantUid='" + merchantUid + '\'' +
                ", amount=" + amount +
                ", payMethod='" + payMethod + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PaymentCompareDTO that = (PaymentCompareDTO) obj;
        return amount == that.amount && 
               (merchantUid != null ? merchantUid.equals(that.merchantUid) : that.merchantUid == null) &&
               (payMethod != null ? payMethod.equals(that.payMethod) : that.payMethod == null);
    }
    
    @Override
    public int hashCode() {
        int result = merchantUid != null ? merchantUid.hashCode() : 0;
        result = 31 * result + amount;
        result = 31 * result + (payMethod != null ? payMethod.hashCode() : 0);
        return result;
    }
} 