package com.blibli.member.controller;

import com.blibli.member.entityDTO.GenericResponse;
import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;
import com.blibli.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/register")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /** Customer Registration */
    @PostMapping("/register")
    public GenericResponse<MemberResponseDTO> register(@RequestBody MemberRegisterRequestDTO request) {
        log.info("Member registration initiated");
        return GenericResponse.<MemberResponseDTO>builder()
                .status("SUCCESS")
                .message("Member registered")
                .data(memberService.register(request))
                .build();
    }

    /** Customer Login */
    @PostMapping("/login")
    public GenericResponse<MemberResponseDTO> login(@RequestBody MemberLoginRequestDTO request) {
        log.info("Member login initiated");
        return GenericResponse.<MemberResponseDTO>builder()
                .status("SUCCESS")
                .message("Login successful")
                .data(memberService.login(request))
                .build();
    }
}
