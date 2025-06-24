package com.koreanair.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
	private int userNo;
    private String userId;
    private String password;
    private String koreanName;
    private String englishName;
    private Date birthDate;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private Timestamp regDate;
    private String kakaoId;
    private String loginType;
    private String profileImage;
    
    // 기본 생성자
    public User() {}
    
    // 매개변수 생성자
    public User(int userNo, String userId, String password, String koreanName, String englishName, 
                Date birthDate, String gender, String email, String phone, String address) {
    	this.userNo = userNo;
        this.userId = userId;
        this.password = password;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    // Getter와 Setter 메소드들
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getKoreanName() {
        return koreanName;
    }
    
    public void setKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Timestamp getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }
    
    public String getKakaoId() {
        return kakaoId;
    }
    
    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }
    
    public String getLoginType() {
        return loginType;
    }
    
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", koreanName='" + koreanName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", regDate=" + regDate +
                ", kakaoId='" + kakaoId + '\'' +
                ", loginType='" + loginType + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
} 