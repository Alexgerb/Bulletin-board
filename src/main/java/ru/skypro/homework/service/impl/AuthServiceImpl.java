package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.RegisterReq;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserDetailsManager manager;

  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

  public AuthServiceImpl(UserDetailsManager manager, PasswordEncoder passwordEncoder,
                         UserRepository userRepository) {
    this.manager = manager;
    this.encoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean login(String userName, String password) {
    if (!manager.userExists(userName)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(userName);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq, Role role) {
    if (manager.userExists(registerReq.getUsername())) {
      return false;
    }

  manager.createUser(
        User.builder()
            .passwordEncoder(this.encoder::encode)
            .password(registerReq.getPassword())
            .username(registerReq.getUsername())
            .roles(role.name())
            .build());
     return true;
  }

}