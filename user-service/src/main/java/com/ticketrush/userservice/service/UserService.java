package com.ticketrush.userservice.service;

import com.ticketrush.commoncore.util.JwtUtil;
import com.ticketrush.userservice.dto.LoginRequestDto;
import com.ticketrush.userservice.dto.SignUpRequestDto;
import com.ticketrush.userservice.entity.User;
import com.ticketrush.userservice.entity.UserRole;
import com.ticketrush.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtUtil jwtUtil;

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

    @Transactional(readOnly = true)
    public void login(LoginRequestDto requestDto, HttpServletResponse response){
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createAccessToken(user.getEmail(),user.getRole().toString());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }

}
