package az.covid.msauthentication.security.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationRequest {
    private String email;

    private String password;

}