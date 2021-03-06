package az.covid.msauthentication.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String token;
    private String role;
    private String status;
    private String userId;
    private String email;
    private Integer countryId;
}
