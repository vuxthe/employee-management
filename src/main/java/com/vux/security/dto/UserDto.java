package com.vux.security.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String name;
	private String email;
	private String codeCheck;
	private LocalTime timeCheckin;
	private LocalTime timeCheckout;
	private String role;
}
