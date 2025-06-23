package com.koreanair.model.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class CheckinDTO {
    private String checkIn;         // check_in (PK, VARCHAR(20))
    private String bookingSeatId;     // booking_seat_id (FK, VARCHAR(50))
    private Timestamp checkInTime;    // check_in_time
    private String checkInType;       // check_in_type
    private int baggageCount;         // baggage_count
    private String baggageTagNumber;  // baggage_tag_number
    private String checkInStatus;     // check_in_status
}