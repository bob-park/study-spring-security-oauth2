package com.example.oauth2clientsociallogin.security.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.PrincipalUser;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.model.User;
import com.example.oauth2clientsociallogin.security.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService extends AbstractOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            user =
                User.builder()
                    .id("1")
                    .username("user1")
                    .password("{noop}12345")
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                    .email("user@user.com")
                    .build();
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(user);

        ProviderUser providerUser = providerUser(providerUserRequest);

        return new PrincipalUser(providerUser);
    }
}
