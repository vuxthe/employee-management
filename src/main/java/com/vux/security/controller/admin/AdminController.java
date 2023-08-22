package com.vux.security.controller.admin;

import com.vux.security.mapper.UserMapper;
import com.vux.security.payload.*;
import com.vux.security.repository.CheckStatusRepository;
import com.vux.security.dto.UserDto;
import com.vux.security.entity.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vux.security.service.impl.AuthenticationService;
import com.vux.security.repository.UserRepository;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
	private final UserRepository userRepository;
	private final AuthenticationService authenticationService;
	private final CheckStatusRepository checkStatusRepository;

	@PostMapping
	public ResponseEntity<CustomApiResponse> createEmployee(@RequestBody RegisterRequest request) throws MessagingException, TemplateException, IOException {
		CustomApiResponse apiResponse;

		Optional<User> user = userRepository.findByEmail(request.getEmail());

		if (user.isPresent()) {
			apiResponse = CustomApiResponse.builder()
					.httpStatus(HttpStatus.BAD_REQUEST)
					.time(LocalDateTime.now())
					.message("Email is existed")
					.build();
			return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
		}

		authenticationService.register(request);

		apiResponse = CustomApiResponse.builder()
				.httpStatus(HttpStatus.OK)
				.time(LocalDateTime.now())
				.message("ok")
				.data(request)
				.build();
		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}
	@PutMapping("/{userId}")
	public ResponseEntity<CustomApiResponse> updateEmployee(
			@PathVariable Integer userId,
			@RequestBody UserDto userDto
	) {

		CustomApiResponse apiResponse;
		Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			apiResponse = CustomApiResponse.builder()
					.httpStatus(HttpStatus.BAD_REQUEST)
					.time(LocalDateTime.now())
					.message("User id not found!")
					.build();
			return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
		}

		User entity = user.get();
		if(!userDto.getName().isEmpty())
			entity.setName(userDto.getName());
		if(!userDto.getEmail().isEmpty())
			entity.setEmail(userDto.getEmail());
		if(!userDto.getCodeCheck().isEmpty())
			entity.setCodeCheck(userDto.getCodeCheck());
		if(userDto.getTimeCheckin() != null)
			entity.setTimeCheckin(userDto.getTimeCheckin());
		if(userDto.getTimeCheckout() != null)
			entity.setTimeCheckout(userDto.getTimeCheckout());
		if(!userDto.getRole().isEmpty())
			entity.setRole(Role.valueOf(userDto.getRole()));

		userRepository.save(entity);

		apiResponse = CustomApiResponse.builder()
				.httpStatus(HttpStatus.OK)
				.time(LocalDateTime.now())
				.message("ok")
				.data(userDto)
				.build();
		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}

	@Transactional(rollbackOn = {ChangeSetPersister.NotFoundException.class})
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable Integer userId) throws ChangeSetPersister.NotFoundException {
			User user = userRepository.findById(userId).orElseThrow(ChangeSetPersister.NotFoundException::new);
			userRepository.delete(user);
			throw new ChangeSetPersister.NotFoundException();
	}



	@GetMapping
	public ResponseEntity<Page<UserDto>> searchUsers(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize
	) {
		if (pageNumber == null)
			pageNumber = 0;
		if (pageSize == null)
			pageSize = 10;

		Pageable pageable;
		Page<User> userList;
		if (keyword == null || keyword.isEmpty()) {
			 pageable = PageRequest.of(pageNumber, pageSize, org.springframework.data.domain.Sort.by("name"));
			 userList = userRepository.findAll(pageable);
		} else {
			 pageable = PageRequest.of(pageNumber , pageSize, org.springframework.data.domain.Sort.by("name"));
			 userList = userRepository.findByNameIgnoreCase(keyword, pageable);
		}

		Page<UserDto> dtoPage = userList.map(UserMapper.MAPPER::mapToUserDto);

		if (dtoPage.isEmpty()) {
			return  ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(dtoPage);
	}

	@GetMapping("/check-info")
	public ResponseEntity<Page<ICheckInfo>> getCheckInfo(
			@RequestParam(required = false) LocalDate begin,
			@RequestParam(required = false) LocalDate end,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize
	) {

		if (pageNumber == null) {
			pageNumber = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}

		Pageable pageable = PageRequest.of(pageNumber , pageSize);;
		Page<ICheckInfo> checkPage;

		if (begin == null && end == null) {
			checkPage = checkStatusRepository.findCheckList(pageable);
		} else {
			checkPage = checkStatusRepository.findCheckListBetween(begin, end, pageable);
		}

		if (checkPage.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(checkPage);
	}

	@GetMapping("/error-check")
	public ResponseEntity<Page<IErrorCheck>> getErrorCheckin(
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize
	) {

		if (pageNumber == null)
			pageNumber = 0;
		if (pageSize == null)
			pageSize = 10;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = LocalDate.now().format(formatter);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Page<IErrorCheck> errorCheck = checkStatusRepository.findErrorCheck(formattedDate, pageable);

		if (errorCheck.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(errorCheck);
	}
}
