package com.blibli.member.service;

import com.blibli.member.entityDTO.LoginResponseDTO;
import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO register(MemberRegisterRequestDTO request);

    LoginResponseDTO login(MemberLoginRequestDTO request);
    MemberResponseDTO getProfile(String token);

    public void logout(String token);

}
