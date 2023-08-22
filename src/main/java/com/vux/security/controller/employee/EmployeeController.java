package com.vux.security.controller.employee;


import com.vux.security.mapper.CheckStatusMapper;
import com.vux.security.payload.CheckInfo;
import com.vux.security.repository.CheckStatusRepository;
import com.vux.security.dto.CheckStatusDto;
import com.vux.security.entity.User;
import com.vux.security.service.CheckStatusService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.vux.security.entity.CheckStatus;
import com.vux.security.mapper.MapStructMapper;
import com.vux.security.payload.CustomApiResponse;
import com.vux.security.repository.UserRepository;



@RestController
@RequestMapping("/api/v1/employee")

@RequiredArgsConstructor
public class EmployeeController {

    private final CheckStatusRepository checkStatusRepository;
    private final UserRepository userRepository;
	private final CheckStatusService checkStatusService;

    @GetMapping
    public ResponseEntity<List<CheckInfo>> getCheckList(
    		@RequestParam(required = false) LocalDate begin,
    		@RequestParam(required = false) LocalDate end
    	) throws NotFoundException {

 		User user = getUsernameBySecurityContext();
		List<CheckInfo> checkList;
    	if (begin == null && end == null) {
    		checkList = checkStatusService.getCheckList(user.getId());
    	} else {
    		checkList = checkStatusService.getCheckListBetween(user.getId(), begin, end);
    	}
		return new ResponseEntity<>(checkList, HttpStatus.OK);
    }

	@PostMapping("check")
	public ResponseEntity<CustomApiResponse> check(@RequestParam String codeCheck) throws NotFoundException {

		User userLogin = getUsernameBySecurityContext();

		Optional<User> user = userRepository.findByCodeCheck(codeCheck);
		CustomApiResponse apiResponse;

		if (user.isEmpty()) {
			 apiResponse = CustomApiResponse.builder()
					.httpStatus(HttpStatus.BAD_REQUEST)
					.time(LocalDateTime.now())
					.message("Invalid code")
					.data("No data")
					.build();
			return ResponseEntity.badRequest().body(apiResponse);
		}
		if (user.get().getId() != userLogin.getId()) {
			apiResponse = CustomApiResponse.builder()
					.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					.time(LocalDateTime.now())
					.message("Code invalid with current user.")
					.build();
			return ResponseEntity.internalServerError().body(apiResponse);
		}

		LocalDate currentDate = LocalDate.now();

		CheckStatus currentCheckStatus = checkStatusRepository.findByUserIdAndDate(userLogin.getId(), currentDate);
		if (currentCheckStatus.getTimeCheckin() != null && currentCheckStatus.getTimeCheckout() != null) {
			apiResponse = CustomApiResponse.builder()
					.httpStatus(HttpStatus.BAD_REQUEST)
					.time(LocalDateTime.now())
					.message("Check-in/check-out is already existed on " + currentDate)
					.build();
			return ResponseEntity.badRequest().body(apiResponse);
		}

		LocalTime currentTime = LocalTime.now();

		if (currentCheckStatus.getTimeCheckin() == null) {
			LocalTime checkinTime = userLogin.getTimeCheckin();

			currentCheckStatus.setTimeCheckin(currentTime);
			currentCheckStatus.setCheckinLate(calcGapTime(checkinTime, currentTime));
		} else {
			LocalTime checkoutTime = userLogin.getTimeCheckout();
			currentCheckStatus.setTimeCheckout(currentTime);

			currentCheckStatus.setCheckoutEarly(calcGapTime(checkoutTime, currentTime));
		}
		checkStatusRepository.save(currentCheckStatus);
		apiResponse = CustomApiResponse.builder()
				.httpStatus(HttpStatus.OK)
				.time(LocalDateTime.now())
				.message("OK")
				.data(CheckStatusMapper.MAPPER.mapToCheckStatusDto(currentCheckStatus))
				.build();
		return ResponseEntity.ok().body(apiResponse);
	}

	private Long calcGapTime(LocalTime start, LocalTime end) {
		Duration duration = Duration.between(start, end);
		return duration.toMinutes();
	}
    
    @GetMapping("error-check")
    public ResponseEntity<CustomApiResponse> errorCheck() throws NotFoundException {

		User user = getUsernameBySecurityContext();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = LocalDate.now().format(formatter);

    	List<CheckStatusDto> checkListDto = checkStatusRepository.findErrorCheckById(user.getId(), formattedDate)
						.stream().map(CheckStatusMapper.MAPPER::mapToCheckStatusDto).collect(Collectors.toList());

		CustomApiResponse apiResponse = CustomApiResponse.builder()
    			.httpStatus(HttpStatus.OK)
    			.time(LocalDateTime.now())
    			.data(checkListDto)
    			.build();
    	return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

	private User getUsernameBySecurityContext() throws NotFoundException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return userRepository.findByEmail(username).orElseThrow(NotFoundException::new);
	}


}
