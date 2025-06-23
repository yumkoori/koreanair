package com.koreanair.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SeatClassDTO {
    private String classId;     // class_id (PK, VARCHAR(20))
    private String className;   // class_name
    private BigDecimal basePrice; // base_price NUMBER(10, 2)
    // ... 기타 seat_class 테이블의 컬럼들 ...
}