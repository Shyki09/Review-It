package com.review.reviewIt.service;

import com.review.reviewIt.entity.CustomUserDetails;
import com.review.reviewIt.entity.User;

import com.review.reviewIt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service

public class CustomUserDetailService implements UserDetailsService {


    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = Optional.ofNullable(userRepository.findByEmail(email))
               .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return CustomUserDetails.buildUserDetails(user);
    }
}
