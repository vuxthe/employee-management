package com.vux.security.payload;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IErrorCheck {

	@Value("#{target.name}")
	String getEmployeeName();

	@Value("#{target.date}")
	LocalDate getDate();
	@Value("#{target.time_checkin}")
	LocalTime getTimeCheckin();
	@Value("#{target.time_checkout}")
	LocalDate getTimeCheckout();

}
