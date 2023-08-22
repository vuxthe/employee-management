package com.vux.security.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.vux.security.entity.User;
import com.vux.security.payload.UserResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @CachePut(value = "user", key = "#email")
  @CacheEvict(allEntries = true, value = "user")
  @Scheduled(fixedDelay = 10 * 60 * 1000 ,  initialDelay = 500)
//  @Cacheable(value = "user", key = "email")
//  @CacheEvict(value = "user", allEntries = true)
  Optional<User> findByEmail(String email);
  Optional<User> findByCodeCheck(String codecheck);

  Page<User> findAll(Pageable pageable);

  @Query(value = "select new com.vux.security.payload.UserResponse(u.name, u.email) from User u")
  List<UserResponse> findUsers();
  Page<User> findByNameIgnoreCase(String keyword, Pageable pageable);
  @Query(value = """
              select distinct
              u.email
              from users u
              inner join check_status c
              on u.id = c.user_id
              where c.time_checkin is null and c.date = :date
        """, nativeQuery = true)
  List<String> getEmailsNotCheckin(@Param("date") LocalDate date);

  @Query(value = """
              select distinct
              u.email
              from users u
              inner join check_status c
              on u.id = c.user_id
              where c.time_checkout is null and c.date = :date
        """, nativeQuery = true)
  List<String> getEmailsNotCheckout(@Param("date") LocalDate date);

}
