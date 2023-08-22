package com.vux.security.config;

import com.vux.security.repository.UserRepository;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@Configuration
@EnableScheduling
@EnableCaching
@Slf4j
public class ApplicationConfig {


  @Autowired
  private UserDetailsService userDetailsService;

  @Value("${spring.mail.host}")
  String host;
  @Value("${spring.mail.port}")
  Integer port;
  @Value("${spring.mail.username}")
  String username;
  @Value("${spring.mail.password}")
  String password;

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Module hibernateModule() {
      return new Hibernate5JakartaModule();
  }

  @Bean
  public JavaMailSender getJavaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost(host);
      mailSender.setPort(port);

      mailSender.setUsername(username);
      mailSender.setPassword(password);
      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");
      return mailSender;
  }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
                GET.name(),
                POST.name(),
                DELETE.name(),
                PUT.name(),
                PATCH.name()
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }

}
