package com.koreanair.model.dto;

public class PaymentPrepareDTO {
	
	private String BookingId;
	private String MerchantUid;
	private String Payment_method;
	private String Amount;
	private String Created_at;
	

	
	public PaymentPrepareDTO(String bookingId, String merchantUid, String payment_method, String amount,
			String created_at) {
		
		this.BookingId = bookingId;
		this.MerchantUid = merchantUid;
		this.Payment_method = payment_method;
		this.Amount = amount;
		this.Created_at = created_at;
	}



	public String getBookingId() {
		return BookingId;
	}



	public void setBookingId(String bookingId) {
		BookingId = bookingId;
	}



	public String getMerchantUid() {
		return MerchantUid;
	}



	public void setMerchantUid(String merchantUid) {
		MerchantUid = merchantUid;
	}



	public String getPayment_method() {
		return Payment_method;
	}



	public void setPayment_method(String payment_method) {
		Payment_method = payment_method;
	}



	public String getAmount() {
		return Amount;
	}



	public void setAmount(String amount) {
		Amount = amount;
	}



	public String getCreated_at() {
		return Created_at;
	}



	public void setCreated_at(String created_at) {
		Created_at = created_at;
	}


	@Override
	public String toString() {
		return "PaymentPrepareDTO [BookingId=" + BookingId + ", MerchantUid=" + MerchantUid + ", Payment_method="
				+ Payment_method + ", Amount=" + Amount + ", Created_at=" + Created_at + "]";
	}
	
	
	
	
	
}