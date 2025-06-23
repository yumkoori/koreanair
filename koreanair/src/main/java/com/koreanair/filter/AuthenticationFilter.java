package com.koreanair.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dto.User;

public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 시 필요한 작업
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());
        
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        if (user == null) {
            // 1. 앞으로 가려던 목적지 URL을 세션에 저장합니다.
            HttpSession sessionToStoreUrl = httpRequest.getSession(); // 세션이 없으면 새로 생성
            String targetUrl = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            if (queryString != null) {
                targetUrl += "?" + queryString;
            }
            
            // LoginHandler에서 사용할 수 있도록 contextPath를 제외한 경로를 저장합니다.
            String relativeTargetUrl = targetUrl.substring(contextPath.length());
            sessionToStoreUrl.setAttribute("targetUrl", relativeTargetUrl);
            
            httpResponse.sendRedirect(contextPath + "/loginForm.do");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // 필터 종료 시 정리 작업
    }
    
    /**
     * 인증이 필요하지 않은 공개 경로인지 확인
     */
    private boolean isPublicPath(String path) {
        // 정적 리소스
        if (path.startsWith("/css/") || path.startsWith("/js/") || 
            path.startsWith("/images/") || path.startsWith("/favicon.ico")) {
            return true;
        }
        
        // views/search 디렉토리는 공개 접근 허용
        if (path.startsWith("/views/search/")) {
            return true;
        }

        
        // 공개 페이지들
        String[] publicPaths = {
            "/",
            "/index.jsp",
            "/login.jsp",
            "/register.jsp",
            "/error.jsp",
            "/loginForm.do",
            "/login.do",
            "/logout.do",
            "/registerForm.do",
            "/register.do",
            "/checkUserId.do",
            "/lookup",
            "/checkinDetail.do", 
            "/checkinApply.do",
            "/airportSearch.do",
            "/search/search.jsp",
            "/views/search/search.jsp",
            "/flightSearch.do",
            "/autocomplete.do",
            "/views/booking/booking.jsp",
            "/booking.do",
            "/passenger.do",
            "/testPayment.do",
            "/payment.jsp",
            "/lookup", // [기존] 비회원 예약 조회를 위해 /lookup 경로 추가
            "/reservationDetail", // [수정] 비회원 예약 상세 조회를 위해 경로 추가
            "/kakao/login.do",
            "/kakao/callback.do",
            "/kakao/signup.do"

        };
        
        for (String publicPath : publicPaths) {
            if (path.equals(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
}