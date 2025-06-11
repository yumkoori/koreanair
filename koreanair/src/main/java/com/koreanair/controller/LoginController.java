package com.koreanair.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreanair.model.dao.UserDAO;
import com.koreanair.model.dto.User;

public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        try {
            switch (action) {
                case "/login.do":
                    handleLogin(request, response);
                    break;
                case "/logout.do":
                    handleLogout(request, response);
                    break;
                case "/register.do":
                    handleRegister(request, response);
                    break;
                case "/registerForm.do":
                    showRegisterForm(request, response);
                    break;
                case "/loginForm.do":
                    showLoginForm(request, response);
                    break;
                case "/dashboard.do":
                    showDashboard(request, response);
                    break;
                case "/deleteAccount.do":
                    handleDeleteAccount(request, response);
                    break;
                case "/checkUserId.do":
                    checkUserId(request, response);
                    break;
                case "/":
                case "/index.do":
                    showMainIndex(request, response);
                    break;
                default:
                    showHome(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "시스템 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/login/error.jsp").forward(request, response);
        }
    }
    
    // 홈 페이지 표시 (기존 로그인 시스템)
    private void showHome(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login/login.jsp").forward(request, response);
    }
    
    // 메인 인덱스 페이지 표시 (새로운 항공사 메인 페이지)
    private void showMainIndex(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    // 로그인 폼 표시
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login/login.jsp").forward(request, response);
    }
    
    // 회원가입 폼 표시
    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
    }
    
    // 대시보드 표시
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // AuthenticationFilter에서 이미 인증을 확인했으므로 바로 대시보드 표시
        request.getRequestDispatcher("/views/login/dashboard.jsp").forward(request, response);
    }
    
    // 로그인 처리
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        
        if (userId == null || password == null || userId.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            request.getRequestDispatcher("/views/login/login.jsp").forward(request, response);
            return;
        }
        
        User user = userDAO.loginUser(userId, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            request.getRequestDispatcher("/views/login/login.jsp").forward(request, response);
        }
    }
    
    // 로그아웃 처리
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    // 회원가입 처리
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String koreanName = request.getParameter("koreanName");
        String englishName = request.getParameter("englishName");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        // 입력값 검증
        if (userId == null || password == null || koreanName == null || englishName == null ||
            birthDateStr == null || gender == null || email == null || phone == null ||
            userId.trim().isEmpty() || password.trim().isEmpty() || koreanName.trim().isEmpty() ||
            englishName.trim().isEmpty() || birthDateStr.trim().isEmpty() || gender.trim().isEmpty() ||
            email.trim().isEmpty() || phone.trim().isEmpty()) {
            request.setAttribute("error", "모든 필수 항목을 입력해주세요.");
            request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
            return;
        }
        
        // 생년월일 형식 검증 및 변환
        Date birthDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(birthDateStr);
            birthDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            request.setAttribute("error", "생년월일 형식이 올바르지 않습니다.");
            request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
            return;
        }
        
        // 아이디 중복 체크
        if (userDAO.isUserIdExists(userId)) {
            request.setAttribute("error", "이미 사용중인 아이디입니다.");
            request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
            return;
        }
        
        // 사용자 등록
        User user = new User(userId, password, koreanName, englishName, birthDate, gender, email, phone, address);
        boolean success = userDAO.insertUser(user);
        
        if (success) {
            request.setAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            request.getRequestDispatcher("/views/login/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "회원가입 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/login/register.jsp").forward(request, response);
        }
    }
    
    // 회원탈퇴 처리
    private void handleDeleteAccount(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // AuthenticationFilter에서 이미 인증을 확인했으므로 세션에서 사용자 정보 가져오기
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String password = request.getParameter("password");
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해주세요.");
            request.getRequestDispatcher("/views/login/dashboard.jsp").forward(request, response);
            return;
        }
        
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            request.setAttribute("error", "비밀번호가 올바르지 않습니다.");
            request.getRequestDispatcher("/views/login/dashboard.jsp").forward(request, response);
            return;
        }
        
        // 회원탈퇴 처리
        boolean success = userDAO.deleteUser(user.getUserId());
        
        if (success) {
            session.invalidate();
            request.setAttribute("message", "회원탈퇴가 완료되었습니다.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "회원탈퇴 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/views/login/dashboard.jsp").forward(request, response);
        }
    }
    
    // 아이디 중복 체크 (AJAX)
    private void checkUserId(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        boolean exists = userDAO.isUserIdExists(userId);
        String jsonResponse = "{\"exists\": " + exists + "}";
        
        response.getWriter().write(jsonResponse);
    }
} 