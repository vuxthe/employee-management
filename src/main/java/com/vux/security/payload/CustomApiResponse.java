package com.vux.security.payload;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor

public class CustomApiResponse {
	
	private HttpStatus httpStatus;
	private LocalDateTime time;
	private String message;
	private int errorCode;
	private Object data;
	
	public CustomApiResponse(HttpStatus httpStatus, LocalDateTime time, String message, int errorCode, Object data) {
		this.httpStatus = httpStatus;
		this.time = time;
		this.message = message;
		this.errorCode = errorCode;
		this.data = data;
	}
	
	public CustomApiResponse(HttpStatus httpStatus, LocalDateTime time, String message, Object data) {
		this.httpStatus = httpStatus;
		this.time = time;
		this.message = message;
		this.data = data;
	}

	
}
