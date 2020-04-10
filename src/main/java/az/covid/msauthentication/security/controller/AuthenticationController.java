package az.covid.msauthentication.security.controller;

import az.covid.msauthentication.security.model.dto.JwtAuthenticationRequest;
import az.covid.msauthentication.security.model.dto.JwtAuthenticationResponse;
import az.covid.msauthentication.security.model.dto.UserInfo;
import az.covid.msauthentication.security.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@Api(value = "Authentication Controller")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @ApiOperation("Create token if input credentials are valid")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody JwtAuthenticationRequest request) {
        logger.debug("Sign in start");
        return service.createAuthenticationToken(request);
    }

    @ApiOperation("if token is valid, returns user information")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public UserInfo validateToken(@RequestHeader("X-Auth-Token") String token) {
        logger.debug("Validate Token start");
        return service.validateToken(token);
    }

}