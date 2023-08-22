package com.vux.security.entity;

import com.vux.security.payload.AuthProvider;
import com.vux.security.payload.Role;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User{

  @Id
  @GeneratedValue
  private Integer id;

  @NotNull
  private String name;

  @Email(message = "email isn't valid")
  @Column(unique = true)
  private String email;
  @NotNull
  private String password;
  @Column(name = "auth_provider")
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @Column(name="code_check",unique = true)
  private String codeCheck;

  @Column(name="time_checkin")
  @Temporal(TemporalType.TIME)
  private LocalTime timeCheckin;
  
  @Column(name="time_checkout")
  @Temporal(TemporalType.TIME)
  private LocalTime timeCheckout;
  
  @Enumerated(EnumType.STRING)
  private Role role;


  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<CheckStatus> checkStatus;
  
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Token> tokens;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<ProjectUser> projectUsers;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Comment> commentList;
}
