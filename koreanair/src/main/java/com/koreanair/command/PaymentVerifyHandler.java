package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.PaymentVerifyDTO;
import com.koreanair.model.service.PaymentVerifyService;
import com.koreanair.util.TokenUtil;

public class PaymentVerifyHandler implements CommandHandler {

    private final PaymentVerifyService paymentVerifyService;

    public PaymentVerifyHandler() {
        this.paymentVerifyService = new PaymentVerifyService();
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 1. CSRF 토큰 검증
            HttpSession session = request.getSession(false);
            String csrfToken = request.getParameter("csrfToken");
            
            if (session == null) {
                System.err.println("[보안 오류] 유효하지 않은 세션에서 결제 검증 시도");
                request.setAttribute("error", "세션이 유효하지 않습니다. 다시 로그인해 주세요.");
                request.setAttribute("verificationStatus", "invalid_session");
                return "/views/error.jsp";
            }
            
            if (!TokenUtil.validateCSRFToken(session, csrfToken)) {
                System.err.println("[보안 오류] CSRF 토큰 검증 실패 - 세션ID: " + session.getId());
                request.setAttribute("error", "보안 토큰이 유효하지 않습니다. 결제 페이지에서 다시 시도해 주세요.");
                request.setAttribute("verificationStatus", "csrf_token_invalid");
                return "/views/error.jsp";
            }
            
            // 2. impUid 파라미터 받기
            String impUid = request.getParameter("imp_uid");

            // 3. impUid 유효성 검사
            if (impUid == null || impUid.trim().isEmpty()) {
                throw new IllegalArgumentException("imp_uid가 누락되었습니다.");
            }

            // 4. PaymentVerifyService를 통한 결제 검증 (아임포트 API vs DB 데이터 비교)
            boolean isValid = paymentVerifyService.verifyPaymentByImpUid(impUid, null);

            if (isValid) {
                // 검증 성공 시 - DB에서 결제 상태 업데이트 (READY -> PAID, paid_at 설정)
                PaymentVerifyDTO paymentInfo = paymentVerifyService.getPaymentInfo(impUid);
                String merchantUid = paymentInfo.getMerchantUid();
                
                // DB 상태 업데이트
                boolean updateSuccess = paymentVerifyService.updatePaymentStatusAfterVerification(merchantUid);
                
                if (updateSuccess) {
                    request.setAttribute("paymentInfo", paymentInfo);
                    request.setAttribute("verificationStatus", "success");
                    
                    System.out.println("결제 검증 및 DB 업데이트 성공 - imp_uid: " + impUid + ", merchant_uid: " + merchantUid);
                    
                    return "/views/payment/success.jsp";
                } else {
                    // DB 업데이트 실패 시
                    request.setAttribute("error", "결제 검증은 성공했지만 DB 업데이트에 실패했습니다. 관리자에게 문의해 주세요.");
                    request.setAttribute("verificationStatus", "db_update_failed");
                    
                    System.err.println("결제 검증은 성공했지만 DB 업데이트 실패 - imp_uid: " + impUid + ", merchant_uid: " + merchantUid);
                    
                    return "/views/error.jsp";
                }
            } else {
                // 검증 실패 시
                request.setAttribute("error", "결제 검증에 실패했습니다. 결제 정보를 확인해 주세요.");
                request.setAttribute("verificationStatus", "failed");
                
                System.err.println("결제 검증 실패 - imp_uid: " + impUid);
                
                return "/views/payment/failure.jsp";
            }

        } catch (IllegalArgumentException e) {
            // 입력값 오류
            System.err.println("[입력 오류] " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.setAttribute("verificationStatus", "invalid_input");
            return "/views/error.jsp";

        } catch (Exception e) {
            // 시스템 오류
            System.err.println("[시스템 오류] 결제 검증 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "시스템 오류로 인해 결제 검증을 완료할 수 없습니다. 잠시 후 다시 시도해 주세요.");
            request.setAttribute("verificationStatus", "system_error");
            return "/views/error.jsp";
        }
    }
}