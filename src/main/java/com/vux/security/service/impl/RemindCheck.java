package com.vux.security.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.vux.security.entity.CheckStatus;
import com.vux.security.entity.User;
import com.vux.security.repository.CheckStatusRepository;
import com.vux.security.repository.UserRepository;
import com.vux.security.service.EmailService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemindCheck {
//	private UserRepository userRepository;
//
//	@Autowired
//	public void setUserRepository(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}

	private final EmailService emailService;
	private final UserRepository userRepository;
	private final CheckStatusRepository checkStatusRepository;

	@Scheduled(cron = "0 0/30 8-9 * * MON-FRI")
    public void remindCheckin(){
		List<String> emailList = userRepository.getEmailsNotCheckin(LocalDate.now());
		emailList.forEach(email -> {
			emailService.sendSimpleMessage(email, "Remind Checkin", "Did you checkin?");
		});
    	
    }
    @Scheduled(cron = "0 0/30 17-18 * * MON-FRI")
    public void remindCheckout(){
    	List<String> emailList = userRepository.getEmailsNotCheckout(LocalDate.now());
		emailList.forEach(email -> {
			emailService.sendSimpleMessage(email, "Remind Checkout", "Did you checkout?");
		});
    }


    @Scheduled(cron = "0 0/30 8 * * MON-FRI")
    public void initCheckStatus() {
    	List<User> userList = userRepository.findAll();
    	LocalDate now = LocalDate.now();
		userList.forEach(user -> {
			int count = checkStatusRepository.countCheckInDay(now, user.getId());
			if (count == 0) {
				CheckStatus entity = CheckStatus.builder()
						.user(user)
						.date(now)
						.build();
				checkStatusRepository.save(entity);
			}
		});
    }
}
