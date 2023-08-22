package com.vux.security.payload;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


public interface ICheckInfo {


	LocalDate getDateCheck();

	String getEmployeeName();
	LocalTime getTimeCheckin();
	Integer getCheckinLate();
	LocalTime getTimeCheckout();
	Integer getCheckoutEarly();

	LocalTime getDefaultTimeCheckin();
	LocalTime getDefaultTimeCheckout();
}
