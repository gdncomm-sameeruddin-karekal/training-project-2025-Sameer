package com.blibli.member.serviceImpl;

import com.blibli.member.entity.MemberEntity;
import com.blibli.member.entityDTO.LoginResponseDTO;
import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;
import com.blibli.member.exception.MemberNotFoundException;
import com.blibli.member.repository.MemberRepository;
import com.blibli.member.service.MemberService;
import com.blibli.member.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@Data
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String BLACKLIST_PREFIX = "blacklisted:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public MemberServiceImpl(MemberRepository repository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MemberResponseDTO register(MemberRegisterRequestDTO request) {

        log.info("Registering new user:{}",request.getUserName());
        if(repository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User Already Existi");
        }

        MemberEntity member = MemberEntity.builder()
                        .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isActive(true)
                .build();
        repository.save(member);

        return MemberResponseDTO.builder()
                .userName(member.getUserName())
                .email(member.getEmail())
                .active(true)
                .build();
    }

    @Override
    public LoginResponseDTO login(MemberLoginRequestDTO request) {
        log.info("Login request: {}", request.getUserName());

        MemberEntity member = repository.findByUserName(request.getUserName())
                .orElseThrow(() -> new MemberNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                member.getId(),
                member.getEmail(),
                member.getRole()
        );

        return LoginResponseDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .token(token)
                .build();
    }

    @Override
    public MemberResponseDTO getProfile(String userId) {
        //Claims claims = jwtUtil.extractAllClaims(token);
        ///String userId = claims.getSubject();

        MemberEntity member = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return MemberResponseDTO.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .email(member.getEmail())
                .role(member.getRole())
                .build();

    }

    @Override
    public void logout(String token) {

        String jwt = token.replace("Bearer ", "");

        long expiry = jwtUtil.getExpirationTime(jwt); // in milliseconds

        redisTemplate.opsForValue().set(
                "blacklist:" + jwt,
                "1",
                Duration.ofMillis(expiry)

        );

        log.info("Token blacklisted successfully");

    }
}
