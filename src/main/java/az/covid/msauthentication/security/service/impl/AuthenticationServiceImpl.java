package az.covid.msauthentication.security.service.impl;

import az.covid.msauthentication.dao.UserRepository;
import az.covid.msauthentication.exception.WrongDataException;
import az.covid.msauthentication.model.entity.UserEntity;
import az.covid.msauthentication.security.exception.AuthenticationException;
import az.covid.msauthentication.security.model.dto.JwtAuthenticationRequest;
import az.covid.msauthentication.security.model.dto.JwtAuthenticationResponse;
import az.covid.msauthentication.security.model.dto.UserInfo;
import az.covid.msauthentication.security.service.AuthenticationService;
import az.covid.msauthentication.security.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(TokenUtil tokenUtil,
                                     UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest request) {
        logger.info("ActionLog.CreateAuthenticationToken.Start");

        authenticate(request.getEmail(), request.getPassword());
        UserEntity userEntity = userRepository.findByEmail(request.getEmail());

        if (userEntity != null && userEntity.getStatus().toString().equals("CONFIRMED")) {
            String userId = userEntity.getId().toString();
            String role = userEntity.getRole().toString();
            String status = userEntity.getStatus().toString();
            String token = tokenUtil.generateToken(request.getEmail(), userId, role, status);

            logger.info("ActionLog.CreateAuthenticationToken.Stop.Success");
            return new JwtAuthenticationResponse(token);
        } else {
            logger.info("ActionLog.CreateAuthenticationToken.Stop.WrongDataException.Thrown");
            throw new WrongDataException("Email is not registered or you are not confirmed by admins");
        }

    }

    public void authenticate(String username, String password) {
        logger.info("ActionLog.Authenticate.Start");
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            logger.error("ActionLog.AuthenticationException.Bad Credentials.Thrown");

            throw new AuthenticationException("Bad credentials", e);
        }

        logger.info("ActionLog.Authenticate.Stop.Success");
    }

    public UserInfo validateToken(String token) {
        logger.info("ActionLog.ValidateToken.Start");
        tokenUtil.isTokenValid(token);
        logger.info("ActionLog.ValidateToken.Stop.Success");

        return tokenUtil.getUserInfoFromToken(token);
    }


}
