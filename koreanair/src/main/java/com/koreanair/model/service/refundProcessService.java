package com.koreanair.model.service;

import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.koreanair.model.dto.RefundProcessDTO;
import com.koreanair.model.dao.RefundProcessDAO;
import com.koreanair.model.dao.refundProcessDAOImpl;

public class refundProcessService {

    private final RefundProcessDAO refundProcessDAO;

    public refundProcessService() {
        this.refundProcessDAO = new refundProcessDAOImpl();
    }

    /**
     * 위변조 검사를 수행하고 merchant_uid를 반환
     * @param dto 환불 검증 정보 (bookingId, userNo)
     * @return merchant_uid (위변조 검사 통과시), null (위변조 검사 실패시)
     * @throws Exception 검사 중 발생하는 예외
     */
    public String validateRefundRequest(RefundProcessDTO dto) throws Exception {
        // 1. 파라미터 유효성 검증
        validateRefundParameters(dto);
        
        // 2. DAO를 통해 위변조 검사 및 merchant_uid 조회
        String merchantUid = refundProcessDAO.validateAndGetMerchantUid(dto);
        
        if (merchantUid == null) {
            System.out.println("[VALIDATION FAILED] 위변조 검사 실패 - BookingId: " + dto.getBookingId() + 
                             ", UserNo: " + dto.getUserNo());
            throw new IllegalArgumentException("유효하지 않은 예약정보입니다. 예약번호와 사용자 정보를 확인해주세요.");
        }
        
        System.out.println("[VALIDATION SUCCESS] 위변조 검사 통과 - MerchantUid: " + merchantUid);
        return merchantUid;
    }

    /**
     * 환불 처리를 수행 (아임포트 API 호출 및 상태 업데이트)
     * @param dto 환불 처리 정보
     * @return 환불 처리 성공 여부
     * @throws Exception 환불 처리 중 발생하는 예외
     */
    public boolean processRefund(RefundProcessDTO dto) throws Exception {
        try {
            // 1. 아임포트 API 호출하여 환불 처리
            boolean refundSuccess = callIamportRefundAPI(dto.getMerchantUid(), dto.getAmount());
            
            if (refundSuccess) {
                // 2. 환불 성공 시 상태 업데이트
                boolean statusUpdateSuccess = refundProcessDAO.updateRefundStatus(dto.getMerchantUid(), "CANCELLED");
                
                if (statusUpdateSuccess) {
                    System.out.println("[SUCCESS] 환불 처리 완료 - MerchantUid: " + dto.getMerchantUid());
                    return true;
                } else {
                    System.err.println("[WARNING] 환불은 성공했으나 상태 업데이트 실패 - MerchantUid: " + dto.getMerchantUid());
                    return false;
                }
            } else {
                // 환불 실패 시 별도 DB 상태 업데이트 없이 false 반환만 수행
                System.err.println("[FAILED] 환불 처리 실패 - MerchantUid: " + dto.getMerchantUid());
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 환불 처리 중 데이터베이스 오류 발생 - MerchantUid: " + dto.getMerchantUid() + ", Error: " + e.getMessage());
            throw new Exception("데이터베이스 연결 또는 쿼리 실행 중 오류가 발생했습니다.", e);
            
        } catch (Exception e) {
            System.err.println("[SYSTEM ERROR] 환불 처리 중 시스템 오류 발생 - MerchantUid: " + dto.getMerchantUid() + ", Error: " + e.getMessage());
            throw new Exception("환불 처리 중 예상치 못한 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 아임포트 API를 호출하여 환불 처리
     * @param merchantUid 주문 고유번호
     * @param amount 환불 금액
     * @return 환불 성공 여부
     * @throws Exception API 호출 중 발생하는 예외
     */
    private boolean callIamportRefundAPI(String merchantUid, String amount) throws Exception {
        try {
            System.out.println("[IAMPORT API] 환불 요청 시작 - MerchantUid: " + merchantUid + ", Amount: " + amount);
            
            // 1. 아임포트 액세스 토큰 발급
            String accessToken = getIamportAccessToken();
            if (accessToken == null) {
                throw new Exception("아임포트 액세스 토큰 발급 실패");
            }
            
            // 2. 환불 API 호출
            boolean refundResult = requestRefund(accessToken, merchantUid, amount);
            
            if (refundResult) {
                System.out.println("[IAMPORT API] 환불 처리 성공 - MerchantUid: " + merchantUid);
                return true;
            } else {
                System.err.println("[IAMPORT API] 환불 처리 실패 - MerchantUid: " + merchantUid);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("[IAMPORT API ERROR] 환불 API 호출 실패 - MerchantUid: " + merchantUid + ", Error: " + e.getMessage());
            throw new Exception("아임포트 환불 API 호출 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 아임포트 액세스 토큰 발급
     * @return 액세스 토큰
     * @throws Exception 토큰 발급 실패시
     */
    private String getIamportAccessToken() throws Exception {
        String tokenUrl = "https://api.iamport.kr/users/getToken";
        
        // TODO: 실제 API KEY와 SECRET으로 변경
        String apiKey = "6276503637885650";
        String apiSecret = "7jajf8d7pzMXanL5PhCbbJfGBfnBO2NZRPqrwFExfSMMYJvJJYcitNUwk0BIrbQKWVh6sOzF1RaxDHTC";
        
        try {
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // 요청 바디 생성
            String requestBody = "{\"imp_key\":\"" + apiKey + "\",\"imp_secret\":\"" + apiSecret + "\"}";
            
            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // 정규표현식으로 code 값 추출
                    Pattern codePattern = Pattern.compile("\"code\"\\s*:\\s*(\\d+)");
                    Matcher codeMatcher = codePattern.matcher(response.toString());
                    
                    if (codeMatcher.find() && "0".equals(codeMatcher.group(1))) {
                        // 정규표현식으로 access_token 추출
                        Pattern tokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"([^\"]+)\"");
                        Matcher tokenMatcher = tokenPattern.matcher(response.toString());
                        
                        if (tokenMatcher.find()) {
                            return tokenMatcher.group(1);
                        } else {
                            throw new Exception("토큰 추출 실패: access_token을 찾을 수 없음");
                        }
                    } else {
                        // 정규표현식으로 메시지 추출
                        Pattern messagePattern = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]+)\"");
                        Matcher messageMatcher = messagePattern.matcher(response.toString());
                        String message = messageMatcher.find() ? messageMatcher.group(1) : "알 수 없는 오류";
                        throw new Exception("토큰 발급 실패: " + message);
                    }
                }
            } else {
                throw new Exception("토큰 발급 API 호출 실패. HTTP 상태코드: " + responseCode);
            }
        } catch (IOException e) {
            throw new Exception("토큰 발급 중 네트워크 오류 발생", e);
        }
    }
    
    /**
     * 환불 요청 실행
     * @param accessToken 아임포트 액세스 토큰
     * @param merchantUid 주문 고유번호
     * @param amount 환불 금액
     * @return 환불 성공 여부
     * @throws Exception 환불 요청 실패시
     */
    private boolean requestRefund(String accessToken, String merchantUid, String amount) throws Exception {
        String refundUrl = "https://api.iamport.kr/payments/cancel";
        
        try {
            URL url = new URL(refundUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setDoOutput(true);
            
            // 환불 요청 바디 생성
            String requestBody = "{\"merchant_uid\":\"" + merchantUid + "\",\"amount\":\"" + amount + "\",\"reason\":\"고객 요청\"}";
            
            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // 정규표현식으로 code 값 추출
                    Pattern codePattern = Pattern.compile("\"code\"\\s*:\\s*(\\d+)");
                    Matcher codeMatcher = codePattern.matcher(response.toString());
                    
                    if (codeMatcher.find() && "0".equals(codeMatcher.group(1))) {
                        // 정규표현식으로 status 추출
                        Pattern statusPattern = Pattern.compile("\"status\"\\s*:\\s*\"([^\"]+)\"");
                        Matcher statusMatcher = statusPattern.matcher(response.toString());
                        
                        if (statusMatcher.find()) {
                            String status = statusMatcher.group(1);
                            return "cancelled".equals(status);
                        } else {
                            System.err.println("환불 실패: status를 찾을 수 없음");
                            return false;
                        }
                    } else {
                        // 정규표현식으로 메시지 추출
                        Pattern messagePattern = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]+)\"");
                        Matcher messageMatcher = messagePattern.matcher(response.toString());
                        String message = messageMatcher.find() ? messageMatcher.group(1) : "알 수 없는 오류";
                        System.err.println("환불 실패: " + message);
                        return false;
                    }
                }
            } else {
                System.err.println("환불 API 호출 실패. HTTP 상태코드: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            throw new Exception("환불 요청 중 네트워크 오류 발생", e);
        }
    }

    /**
     * 환불 요청 파라미터 유효성 검증
     * @param dto 검증할 DTO 객체
     * @throws IllegalArgumentException 유효하지 않은 입력값인 경우
     */
    private void validateRefundParameters(RefundProcessDTO dto) throws IllegalArgumentException {
        if (dto == null) {
            throw new IllegalArgumentException("환불 요청 정보가 누락되었습니다.");
        }
        
        // 예약번호 검증
        String bookingId = dto.getBookingId();
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("예약번호가 누락되었습니다.");
        }
        
        // 사용자번호 검증
        int userNo = dto.getUserNo();
        if (userNo <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 정보입니다.");
        }
        
        System.out.println("[VALIDATION] 환불 파라미터 검증 완료 - BookingId: " + bookingId + ", UserNo: " + userNo);
    }

    /**
     * merchant_uid로 결제 금액 조회
     * @param merchantUid 주문 고유번호
     * @return 결제 금액
     * @throws Exception 조회 중 발생하는 예외
     */
    public String getPaymentAmount(String merchantUid) throws Exception {
        if (merchantUid == null || merchantUid.trim().isEmpty()) {
            throw new IllegalArgumentException("merchant_uid가 누락되었습니다.");
        }
        
        try {
            String paymentAmount = refundProcessDAO.getPaymentAmount(merchantUid);
            
            if (paymentAmount == null) {
                System.out.println("[WARNING] 결제 금액 조회 실패 - MerchantUid: " + merchantUid);
                throw new IllegalArgumentException("해당 merchant_uid에 대한 결제 정보를 찾을 수 없습니다.");
            }
            
            System.out.println("[SUCCESS] 결제 금액 조회 성공 - MerchantUid: " + merchantUid + ", Amount: " + paymentAmount);
            return paymentAmount;
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] 결제 금액 조회 중 데이터베이스 오류 발생 - MerchantUid: " + merchantUid + ", Error: " + e.getMessage());
            throw new Exception("데이터베이스 연결 또는 쿼리 실행 중 오류가 발생했습니다.", e);
            
        } catch (Exception e) {
            System.err.println("[SYSTEM ERROR] 결제 금액 조회 중 시스템 오류 발생 - MerchantUid: " + merchantUid + ", Error: " + e.getMessage());
            throw new Exception("결제 금액 조회 중 예상치 못한 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
} 