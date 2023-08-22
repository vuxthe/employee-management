package com.vux.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class CheckInfo {
    private LocalDate date;
    private LocalTime timeCheckin;
    private Long checkinLate;
    private LocalTime timeCheckout;
    private Long checkoutEarly;
}
