package com.helios.platform.sentinel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecoverRequestDTO {
    private String username;
    private String secretPhrase;
    private String newPassword;
}
