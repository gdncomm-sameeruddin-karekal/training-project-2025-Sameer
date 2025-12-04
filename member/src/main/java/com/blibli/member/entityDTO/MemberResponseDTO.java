package com.blibli.member.entityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {

    private String id;
    private String userName;
    private String email;
    private boolean active;
    private String role;

}
