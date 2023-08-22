package com.vux.security.service.impl;

import com.vux.security.entity.User;
import com.vux.security.payload.UserDetailsImpl;
import com.vux.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");

        return UserDetailsImpl.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .role(user.get().getRole())
                .build();
    }
}
