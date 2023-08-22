package com.vux.security.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.vux.security.entity.CheckStatus;
import com.vux.security.payload.CheckInfo;
import com.vux.security.payload.ICheckInfo;
import com.vux.security.payload.IErrorCheck;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckStatusRepository extends JpaRepository<CheckStatus, Integer> {
	@Query(value = "select new com.vux.security.payload.CheckInfo(c.date, c.timeCheckin, c.checkinLate, c.timeCheckout, c.checkoutEarly) from CheckStatus c where c.user.id = ?1")
	List<CheckInfo> findCheckListById(Integer userId);
	
	@Query(value = "select new com.vux.security.payload.CheckInfo(c.date, c.timeCheckin, c.checkinLate, c.timeCheckout, c.checkoutEarly) from CheckStatus c where c.date between ?2 and ?3 and c.user.id = ?1")
	List<CheckInfo> findCheckListByIdBetween(Integer userId, LocalDate begin, LocalDate end);


	@Query(value="select count(*)\r\n"
			+ "from check_status\r\n"
			+ "where date = ?1 and user_id = ?2\r\n", nativeQuery=true)
	Integer countCheckInDay(LocalDate date, Integer userId);




	@Query(value="select *\r\n"
			+ "from check_status\r\n"
			+ "where (time_checkin is null or time_checkout is null)\r\n"
			+ "and DATE_FORMAT(date, '%Y-%m') = ?2\r\n"
			+ "and user_id = ?1", nativeQuery=true)
	List<CheckStatus> findErrorCheckById(Integer userId, String date);

	@Query(value= """
			select 
				u.name,
				c.date,
				c.time_checkin,
				c.time_checkout
			from users u
			inner join check_status c on u.id = c.user_id
			where (c.time_checkin is null or c.time_checkout is null)
			and date_format(date, '%Y-%m') = ?1
		""", countQuery = "select count(*) from check_status c inner join users u on c.user_id = u.id", nativeQuery = true)
	Page<IErrorCheck> findErrorCheck(String date, Pageable pageable);

	@Query(value = """
 			select c.date as dateCheck,
 				u.name as employeeName,
 				c.timeCheckin as timeCheckin,
 				u.timeCheckin as defaultTimeCheckin,
 				c.checkinLate as checkinLate,
 				c.timeCheckout as timeCheckout,
 				u.timeCheckout as defaultTimeCheckout,
 				c.checkoutEarly as checkoutEarly
 			from CheckStatus c
 			inner join User u on c.user.id = u.id
 		""", countQuery = "select count(c) from CheckStatus c"
	)
	Page<ICheckInfo> findCheckList(Pageable pageable);



	@Query(value = """
		 		select c.date as dateCheck,
 				u.name as employeeName,
 				c.timeCheckin as timeCheckin,
 				u.timeCheckin as defaultTimeCheckin,
 				c.checkinLate as checkinLate,
 				c.timeCheckout as timeCheckout,
 				u.timeCheckout as defaultTimeCheckout,
 				c.checkoutEarly as checkoutEarly
 			from CheckStatus c
 			inner join User u on c.user.id = u.id
 			where c.date between ?1 and ?2
		""", countQuery = "select count(c)from CheckStatus c"
	)
	Page<ICheckInfo> findCheckListBetween(LocalDate begin, LocalDate end, Pageable pageable);
	


	@Query(value = "select c from CheckStatus c where c.user.id = :userId and c.date = :currentDate")
    CheckStatus findByUserIdAndDate(@Param("userId") Integer id, @Param("currentDate") LocalDate currentDate);
}
