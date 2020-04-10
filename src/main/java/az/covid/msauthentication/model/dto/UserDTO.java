package az.covid.msauthentication.model.dto;

import az.covid.msauthentication.validation.user.UserConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserConstraint
public class UserDTO {

    private String email;
    private String name;
    private String surname;
    private String password;
}
