package com.koreanair.util;

import javax.servlet.http.HttpServletRequest;

public class KakaoConfig {
    // 카카오 개발자 콘솔에서 발급받은 REST API 키
    public static final String KAKAO_REST_API_KEY = "4d731a367ed0f86cd8a1cd7018e47412";
    
    // 카카오 API URL들
    public static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    public static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    public static final String KAKAO_BROWSER_LOGOUT_URL = "https://kauth.kakao.com/oauth/logout";
    
    // 동적으로 Redirect URI 생성
    public static String getRedirectUri(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuilder redirectUri = new StringBuilder();
        redirectUri.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            redirectUri.append(":").append(serverPort);
        }
        
        redirectUri.append(contextPath).append("/kakao/callback.do");
        return redirectUri.toString();
    }
    
    // 카카오 로그인 URL 생성
    public static String getKakaoLoginUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append(KAKAO_AUTH_URL);
        url.append("?client_id=").append(KAKAO_REST_API_KEY);
        url.append("&redirect_uri=").append(getRedirectUri(request));
        url.append("&response_type=code");
        return url.toString();
    }
    
    // 카카오 브라우저 로그아웃 URL 생성
    public static String getKakaoBrowserLogoutUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append(KAKAO_BROWSER_LOGOUT_URL);
        url.append("?client_id=").append(KAKAO_REST_API_KEY);
        
        // 로그아웃 후 리다이렉트할 URL 설정
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuilder logoutRedirectUri = new StringBuilder();
        logoutRedirectUri.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            logoutRedirectUri.append(":").append(serverPort);
        }
        
        logoutRedirectUri.append(contextPath).append("/");
        url.append("&logout_redirect_uri=").append(logoutRedirectUri.toString());
        
        return url.toString();
    }
} 