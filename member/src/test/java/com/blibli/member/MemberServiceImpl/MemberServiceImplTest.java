package com.blibli.member.MemberServiceImpl;

import com.blibli.member.entity.MemberEntity;
import com.blibli.member.entityDTO.LoginResponseDTO;
import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;
import com.blibli.member.exception.MemberNotFoundException;
import com.blibli.member.repository.MemberRepository;
import com.blibli.member.serviceImpl.MemberServiceImpl;
import com.blibli.member.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private MemberServiceImpl memberService;
    // -------------------------------------------------------
    //  REGISTER MEMBER TESTS
    // -------------------------------------------------------
    @Test
    void registerMember_success() {
        MemberRegisterRequestDTO req =
                new MemberRegisterRequestDTO("john123", "password", "john@test.com");
        when(memberRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");
        MemberEntity saved = MemberEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .userName("john123")
                .password("encodedPass")
                .email("john@test.com")
                .isActive(true)
                .build();
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(saved);
        MemberResponseDTO response = memberService.register(req);
        assertEquals("john123", response.getUserName());
        assertEquals("john@test.com", response.getEmail());
        assertTrue(response.isActive());
    }
    @Test
    void registerMember_emailExists() {
        MemberRegisterRequestDTO req =
                new MemberRegisterRequestDTO("john123", "password", "john@test.com");
        when(memberRepository.existsByEmail("john@test.com")).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> memberService.register(req));
    }

    // -------------------------------------------------------
    //  LOGIN MEMBER TESTS
    // -------------------------------------------------------
    @Test
    void loginMember_success() {
        UUID id = UUID.randomUUID();
        MemberEntity member = MemberEntity.builder()
                .id(String.valueOf(id))
                .userName("john123")
                .password("encodedPass")
                .email("john@test.com")
                .isActive(true)
                .build();
        when(memberRepository.findByUserName("john123"))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches("password", "encodedPass"))
                .thenReturn(true);
//        when(jwtUtil.generateToken(any(UUID.class)),anyString(),"sdfsdf");
//                .thenReturn("jwt-token");
        MemberLoginRequestDTO req =
                new MemberLoginRequestDTO("john123", "password");

        LoginResponseDTO response = memberService.login(req);
        assertEquals("jwt-token", response.getToken());
    }
    @Test
    void loginMember_invalidPassword() {
        MemberEntity member = MemberEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .userName("john123")
                .password("encodedPass")
                .isActive(true)
                .build();
        when(memberRepository.findByUserName("john123"))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.matches("password", "encodedPass"))
                .thenReturn(false);
        MemberLoginRequestDTO req =
                new MemberLoginRequestDTO("john123", "password");
        assertThrows(RuntimeException.class,
                () -> memberService.login(req));
    }
    @Test
    void loginMember_userNotFound() {
        MemberLoginRequestDTO req =
                new MemberLoginRequestDTO("john123", "password");
        when(memberRepository.findByUserName("john123"))
                .thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class,
                () -> memberService.login(req));
    }
}

