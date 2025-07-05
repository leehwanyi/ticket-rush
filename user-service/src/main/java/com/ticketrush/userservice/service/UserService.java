package com.ticketrush.userservice.service;

import com.ticketrush.userservice.dto.SignUpRequestDto;
import com.ticketrush.userservice.entity.User;
import com.ticketrush.userservice.entity.UserRole;
import com.ticketrush.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequestDto requestDto){
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(email)
                .password(password)
                .username(requestDto.getUsername())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

}
