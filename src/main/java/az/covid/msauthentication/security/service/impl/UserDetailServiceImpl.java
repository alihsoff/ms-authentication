package az.covid.msauthentication.security.service.impl;

import az.covid.msauthentication.dao.UserRepository;
import az.covid.msauthentication.exception.WrongDataException;
import az.covid.msauthentication.model.entity.UserEntity;
import az.covid.msauthentication.security.bean.CustomUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = repository.findByEmail(username);
        if (user != null) {
            return buildSecurityUser(user);
        } else {
            throw new WrongDataException("No such email is registered");
        }

    }

    private CustomUserDetail buildSecurityUser(UserEntity user) {
        return CustomUserDetail.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(user.getRole()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true).build();
    }
}