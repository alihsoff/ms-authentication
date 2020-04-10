package az.covid.msauthentication.security.service;

import az.covid.msauthentication.security.model.dto.JwtAuthenticationRequest;
import az.covid.msauthentication.security.model.dto.JwtAuthenticationResponse;
import az.covid.msauthentication.security.model.dto.UserInfo;

public interface AuthenticationService {

    JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest request);

    void authenticate(String username, String password);

    UserInfo validateToken(String token);
}
