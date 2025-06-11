package com.koreanair.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBconn2 {

    // --- DB 접속 정보 (MariaDB 환경에 맞게 수정) ---
    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String URL = "jdbc:mariadb://air.chkmcmk8aoyu.ap-northeast-2.rds.amazonaws.com:3306/air_db";
    private static final String USER = "admin";
    private static final String PASSWORD = "Ssqwer1234!!";

    // JDBC 드라이버 로딩
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MariaDB JDBC 드라이버 로딩에 실패했습니다.");
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 커넥션을 반환하는 메소드
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 사용한 자원을 안전하게 닫아주는 메소드 (SELECT 쿼리용)
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 사용한 자원을 안전하게 닫아주는 메소드 (INSERT, UPDATE, DELETE 쿼리용)
     */
    public static void close(Connection conn, Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}