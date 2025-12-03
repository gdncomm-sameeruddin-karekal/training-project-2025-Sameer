package com.blibli.member.serviceImpl;

import com.blibli.member.entity.MemberEntity;
import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;
import com.blibli.member.exception.MemberNotFoundException;
import com.blibli.member.repository.MemberRepository;
import com.blibli.member.service.MemberService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
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
    public MemberResponseDTO login(MemberLoginRequestDTO request) {
        log.info("Login request: {}", request.getUserName());

        MemberEntity member = repository.findByUserName(request.getUserName())
                .orElseThrow(() -> new MemberNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return MemberResponseDTO.builder()
                .userName(member.getUserName())
                .email(member.getEmail())
                .active(member.getIsActive())
                .build();
    }
}
