package com.ead.authuser.configs.security;

import com.ead.authuser.Repositories.UserRepository;
import com.ead.authuser.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserModel userModel = userRepository.findByUserName(username)
            .orElseThrow(()-> new UsernameNotFoundException("User not found whit username: "+ username));
    return UserDetailsImpl.build(userModel);
  }
  public UserDetails loadUserById(UUID userId) throws AuthenticationCredentialsNotFoundException {
    UserModel userModel = userRepository.findById(userId)
            .orElseThrow(()-> new AuthenticationCredentialsNotFoundException("User not found whit userId: "+ userId));
    return UserDetailsImpl.build(userModel);
  }

}
