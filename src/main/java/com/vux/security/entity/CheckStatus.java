package com.vux.security.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CheckStatus {
	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	@Temporal(TemporalType.DATE)
	private LocalDate date;
	@Temporal(TemporalType.TIME)
	@Column(name = "time_checkin")
	private LocalTime timeCheckin;

	@Column(name = "checkin_late")
	private Long checkinLate;

	@Temporal(TemporalType.TIME)
	@Column(name = "time_checkout")
	private LocalTime timeCheckout;

	@Column(name = "checkout_early")
	private Long checkoutEarly;
}
