package com.blibli.member.entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String memberId;
    private String email;
    private String token;
}
