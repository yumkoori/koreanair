package com.koreanair.command;

import java.util.List; // [추가] List import
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.ReservationDTO; // [추가] ReservationDTO import
import com.koreanair.model.dto.User;

public class LoginHandler implements CommandHandler {
    private UserDAO userDAO;
    
    public LoginHandler() {
        this.userDAO = new UserDAO();
    }
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        switch (action) {
            case "/login.do":
                return handleLogin(request, response);
            case "/loginForm.do":
                return showLoginForm(request, response);
            case "/checkUserId.do":
                return checkUserId(request, response);
            default:
                return "/views/login/login.jsp";
        }
    }
    
    // 로그인 폼 표시
    private String showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // 세션에서 메시지들 확인
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 회원가입 성공 메시지 확인
            String signupSuccessMessage = (String) session.getAttribute("signupSuccessMessage");
            if (signupSuccessMessage != null) {
                request.setAttribute("message", signupSuccessMessage);
                session.removeAttribute("signupSuccessMessage");
            }
            
            // 카카오 연동 성공 메시지 확인
            String linkSuccessMessage = (String) session.getAttribute("linkSuccessMessage");
            if (linkSuccessMessage != null) {
                request.setAttribute("message", linkSuccessMessage);
                session.removeAttribute("linkSuccessMessage");
            }
        }
        
        // 저장된 아이디 쿠키 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("savedUserId".equals(cookie.getName())) {
                    String savedUserId = cookie.getValue();
                    if (savedUserId != null && !savedUserId.trim().isEmpty()) {
                        request.setAttribute("savedUserId", savedUserId);
                        request.setAttribute("rememberChecked", true);
                    }
                    break;
                }
            }
        }
        
        return "/views/login/login.jsp";
    }
    
    // 로그인 처리
    private String handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        if (userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            return "/views/login/login.jsp";
        }
        
        User user = userDAO.loginUser(userId, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);


            // ▼▼▼ [추가된 코드] 로그인 성공 시 예약 목록을 조회하여 세션에 저장 ▼▼▼
            List<ReservationDTO> userBookings = userDAO.getUserReservations(user.getUserId());
            session.setAttribute("userBookings", userBookings);
            // ▲▲▲ [추가된 코드 끝] ▲▲▲

            // 1. 세션에서 저장해둔 목적지 URL을 가져옵니다.
            String targetUrl = (String) session.getAttribute("targetUrl");
            session.removeAttribute("targetUrl"); // 사용 후에는 즉시 세션에서 제거
            if ("on".equals(remember)) {
            	// 아이디 저장 체크박스가 선택된 경우 - 30일간 쿠키 저장
            	Cookie userIdCookie = new Cookie("savedUserId", userId.trim());
            	userIdCookie.setMaxAge(30 * 24 * 60 * 60); // 30일
            	userIdCookie.setPath("/");
            	response.addCookie(userIdCookie);
            } else {
            	// 체크박스가 선택되지 않은 경우 - 기존 쿠키 삭제
            	Cookie userIdCookie = new Cookie("savedUserId", "");
            	userIdCookie.setMaxAge(0);
            	userIdCookie.setPath("/");
            	response.addCookie(userIdCookie);
            }

            if (targetUrl != null && !targetUrl.isEmpty()) {
                // 2. 목적지가 있으면 해당 URL로 리디렉션합니다.
                return "redirect:" + targetUrl;
            } else {
                // 3. 목적지가 없으면 기존처럼 메인 페이지로 리디렉션합니다.
                return "redirect:/";
            }

        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "/views/login/login.jsp";
        }
    }
    
    // 아이디 중복 체크 (AJAX 요청용)
    private String checkUserId(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String userId = request.getParameter("userId");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"exists\": true}");
            return null; // AJAX 응답이므로 뷰 없음
        }
        
        boolean exists = userDAO.isUserIdExists(userId);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
        
        return null; // AJAX 응답이므로 뷰 없음
    }
}