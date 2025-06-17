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
        
        // 인증이 필요하지 않은 경로들
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 세션에서 사용자 정보 확인
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (user == null) {
            httpResponse.sendRedirect(contextPath + "/loginForm.do");
            return;
        }
        
        // 인증된 사용자는 요청 계속 처리
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
            "/airportSearch.do",
            "/search/search.jsp",
            "/views/search/search.jsp",
            "/flightSearch.do",
            "/autocomplete.do",
            "/views/booking/booking.jsp",
            "/booking.do"
            
        };
        
        for (String publicPath : publicPaths) {
            if (path.equals(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
} 