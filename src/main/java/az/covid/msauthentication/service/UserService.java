package az.covid.msauthentication.service;


import az.covid.msauthentication.model.dto.UserDTO;

public interface UserService {

    void signUp(UserDTO userDTO);

    String getCustomerIdByEmail(String token, String email);
}
