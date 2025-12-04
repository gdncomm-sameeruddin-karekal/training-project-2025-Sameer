package com.blibli.member.controller;

import com.blibli.member.entityDTO.*;
import com.blibli.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
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
    //public GenericResponse<LoginResponseDTO> login(@RequestBody MemberLoginRequestDTO request) {
    public LoginResponseDTO login(@RequestBody MemberLoginRequestDTO request){
//        log.info("Member login initiated");
//        return GenericResponse.<LoginResponseDTO>builder()
//                .status("SUCCESS")
//                .message("Login successful")
//                .data(memberService.login(request))
//                .build();
        return memberService.login(request);
    }
    @GetMapping("/profile")
    public GenericResponse<MemberResponseDTO> getProfile(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        //return memberService.getProfile(token);
        return GenericResponse.<MemberResponseDTO>builder()
                .status("SUCCESS")
                .message("Member registered")
                .data(memberService.getProfile(token))
                .build();
    }
}
