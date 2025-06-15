package com.koreanair.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnection {
    private static DataSource dataSource;
    
    /**
     * 커넥션 풀에서 커넥션을 가져옵니다.
     * @return Connection 객체
     * @throws SQLException 데이터베이스 연결 오류
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }
    
    private static synchronized void initializeDataSource() throws SQLException {
        if (dataSource != null) {
            return; // 이미 초기화됨
        }
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/airloginDB");
            System.out.println("데이터소스 초기화 성공");
        } catch (Exception e) {
            System.err.println("데이터소스 초기화 실패: " + e.getMessage());
            throw new SQLException("데이터소스 초기화 실패: " + e.getMessage(), e);
        }
    }
    
    /**
     * 커넥션을 커넥션 풀로 반환합니다.
     * @param conn 반환할 Connection 객체
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 