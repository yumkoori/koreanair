package com.koreanair.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.PaymentPrepareDTO;
import com.koreanair.model.service.PaymentPrepareService;
import com.koreanair.util.TokenUtil;

public class PaymentPrepareHandler implements CommandHandler {

    private final PaymentPrepareService paymentPrepareService;

    public PaymentPrepareHandler() {
        this.paymentPrepareService = new PaymentPrepareService();
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/plain; charset=UTF-8");
        
        try {
            // 1. 세션 및 이중결제 방지 토큰 검증
            HttpSession session = request.getSession(false);
            String paymentToken = request.getParameter("paymentToken");
            
            if (session == null) {
                response.getWriter().write("invalid_session: 세션이 유효하지 않습니다.");
                return null;
            }
            
            if (!TokenUtil.validateAndConsumePaymentToken(session, paymentToken)) {
                response.getWriter().write("invalid_token: 유효하지 않은 결제 토큰입니다. 페이지를 새로고침 후 다시 시도해주세요.");
                return null;
            }
            
            // 2. 요청 파라미터 추출
            String merchantUid = request.getParameter("merchantUid");
            String bookingId = request.getParameter("bookingId");      
            String payment_method = request.getParameter("payment_method"); 
            String amount = request.getParameter("amount");
            String created_at = request.getParameter("created_at");
            
            // 3. PaymentPrepareDTO 생성
            PaymentPrepareDTO dto = new PaymentPrepareDTO(bookingId, merchantUid, payment_method, amount, created_at);
            
            // 4. 서비스 계층에 처리 위임
            boolean success = paymentPrepareService.processPaymentPrepare(dto);

            if (success) {
                response.getWriter().write("success");
            } else {
                response.getWriter().write("failed");
            }

        } catch (IllegalArgumentException e) {
            // 입력값 검증 오류 처리
            response.getWriter().write("invalid_input: " + e.getMessage());
            
        } catch (Exception e) {
            // 시스템 오류 처리
            response.getWriter().write("system_error: 결제 처리 중 오류가 발생했습니다.");
        }
        
        return null; // AJAX 응답이므로 JSP 페이지로 이동하지 않음
    }
}
