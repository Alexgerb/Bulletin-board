package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.RegisterReq;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserDetailsManager manager;

  private final PasswordEncoder encoder;
  private final UserProfileRepository userProfileRepository;

  public AuthServiceImpl(UserDetailsManager manager, PasswordEncoder passwordEncoder,
                         UserProfileRepository userProfileRepository) {
    this.manager = manager;
    this.encoder = passwordEncoder;
    this.userProfileRepository = userProfileRepository;
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

    UserProfile userProfile = createUser(registerReq, role);

    if (manager.userExists(registerReq.getUsername())) {
      return false;
    }

    saveUserProfile(userProfile);


    manager.createUser(
        User.builder()
            .passwordEncoder(this.encoder::encode)
            .password(registerReq.getPassword())
            .username(registerReq.getUsername())
            .roles(role.name())
            .build());
     return true;
  }

  private UserProfile createUser(RegisterReq registerReq, Role role) {
    UserProfile userProfile = new UserProfile(
            registerReq.getUsername(),
            registerReq.getUsername(),
            registerReq.getFirstName(),
            registerReq.getLastName(),
            registerReq.getPhone(),
            role,
            registerReq.getPassword()
    );
    return userProfile;
  }

  private void saveUserProfile(UserProfile userProfile) {
    userProfileRepository.save(userProfile);
  }



}
