package com.vux.security.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckStatusDto {
	private Integer userId;
	private LocalDate date;
	private LocalTime timeCheckin;
	private Long checkinLate;
	private LocalTime timeCheckout;
	private Long checkoutEarly;
}
