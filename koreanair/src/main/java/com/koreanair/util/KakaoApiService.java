package com.koreanair.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KakaoApiService {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 인가 코드로 액세스 토큰 요청
     */
    public static String getAccessToken(String code, String redirectUri) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(KakaoConfig.KAKAO_TOKEN_URL);
        
        // 요청 파라미터 설정
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_id", KakaoConfig.KAKAO_REST_API_KEY));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("code", code));
        
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity, "UTF-8");
        
        // JSON 파싱하여 access_token 추출
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }
    
    /**
     * 액세스 토큰으로 사용자 정보 요청
     */
    public static KakaoUserInfo getUserInfo(String accessToken) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(KakaoConfig.KAKAO_USER_INFO_URL);
        
        // Authorization 헤더 설정
        httpGet.setHeader("Authorization", "Bearer " + accessToken);
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity, "UTF-8");
        
        // JSON 파싱하여 사용자 정보 추출
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        KakaoUserInfo userInfo = new KakaoUserInfo();
        userInfo.setId(jsonNode.get("id").asText());
        
        JsonNode kakaoAccount = jsonNode.get("kakao_account");
        if (kakaoAccount != null) {
            if (kakaoAccount.has("email")) {
                userInfo.setEmail(kakaoAccount.get("email").asText());
            }
            
            JsonNode profile = kakaoAccount.get("profile");
            if (profile != null) {
                if (profile.has("nickname")) {
                    userInfo.setNickname(profile.get("nickname").asText());
                }
                if (profile.has("profile_image_url")) {
                    userInfo.setProfileImageUrl(profile.get("profile_image_url").asText());
                }
            }
        }
        
        return userInfo;
    }
    
    /**
     * 카카오 사용자 정보를 담는 내부 클래스
     */
    public static class KakaoUserInfo {
        private String id;
        private String email;
        private String nickname;
        private String profileImageUrl;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getNickname() {
            return nickname;
        }
        
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        
        public String getProfileImageUrl() {
            return profileImageUrl;
        }
        
        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
    
    /**
     * 카카오 로그아웃 (액세스 토큰으로 카카오 세션 만료)
     */
    public static boolean logoutKakao(String accessToken) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(KakaoConfig.KAKAO_LOGOUT_URL);
            
            // Authorization 헤더 설정
            httpPost.setHeader("Authorization", "Bearer " + accessToken);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            
            // 200 OK 또는 204 No Content면 성공
            return statusCode == 200 || statusCode == 204;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 