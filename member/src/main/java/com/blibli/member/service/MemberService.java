package com.blibli.member.service;

import com.blibli.member.entityDTO.MemberLoginRequestDTO;
import com.blibli.member.entityDTO.MemberRegisterRequestDTO;
import com.blibli.member.entityDTO.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO register(MemberRegisterRequestDTO request);

    MemberResponseDTO login(MemberLoginRequestDTO request);

}
