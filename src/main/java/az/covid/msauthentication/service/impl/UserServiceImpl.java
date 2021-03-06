package az.covid.msauthentication.service.impl;


import az.covid.msauthentication.dao.UserRepository;
import az.covid.msauthentication.exception.WrongDataException;
import az.covid.msauthentication.model.dto.MailDTO;
import az.covid.msauthentication.model.dto.UserDTO;
import az.covid.msauthentication.model.entity.UserEntity;
import az.covid.msauthentication.security.exception.AuthenticationException;
import az.covid.msauthentication.security.model.Role;
import az.covid.msauthentication.security.model.Status;
import az.covid.msauthentication.security.model.dto.UserInfo;
import az.covid.msauthentication.security.service.AuthenticationService;
import az.covid.msauthentication.service.EmailService;
import az.covid.msauthentication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    public void signUp(UserDTO userDTO) {
        logger.info("ActionLog.Sign up user.Start");

        MailDTO mail = new MailDTO().builder()
                .mailTo(userDTO.getEmail())
                .mailSubject("Your registration letter")
                .mailBody("<html><body>You are registered for our app</body></html>")
                .build();

        emailService.sendToQueue(mail);

        UserEntity checkedEmail = userRepository.findByEmail(userDTO.getEmail());
        if (checkedEmail != null) {
            logger.error("ActionLog.WrongDataException.Thrown");
            throw new WrongDataException("This email already exists");
        }

        String password = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        UserEntity userEntity = UserEntity
                .builder()
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .username(userDTO.getEmail())
                .email(userDTO.getEmail())
                .password(password)
                .role(Role.ROLE_USER)
                .status(Status.REGISTERED)
                .build();

        userRepository.save(userEntity);
        logger.info("ActionLog.Sign up user.Stop.Success");

    }

    public String getCustomerIdByEmail(String token, String email) {
        logger.info("ActionLog.GetCustomerIdByEmail.Start");
        UserInfo userInfo = authenticationService.validateToken(token);
        String userRole = userInfo.getRole();
        if (!userRole.equals("ROLE_ADMIN")) {
            logger.error("ActionLog.AuthenticationException.Thrown");
            throw new AuthenticationException("You do not have rights for access");
        }

        UserEntity foundUser = userRepository.findByEmail(email);
        if (foundUser != null) {
            logger.info("ActionLog.GetCustomerIdByEmail.Stop.Success");
            return foundUser.getId().toString();
        } else {
            logger.error("ActionLog.WrongDataException.Thrown");
            throw new WrongDataException("No such email is found");
        }

    }
}
