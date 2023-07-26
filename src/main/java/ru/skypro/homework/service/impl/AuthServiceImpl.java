package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.RegisterReq;
import ru.skypro.homework.model.entity.Role;
import ru.skypro.homework.repository.RoleRepository;
import ru.skypro.homework.security.JpaUserDetailsService;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.AuthService;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


  private final JpaUserDetailsService manager;

  private final PasswordEncoder encoder;

  private final RoleRepository roleRepository;

  private final MyUserDetailsService myUserDetailsService;

  /**
   * метод авторизации пользователя
   * */
  @Override
  public boolean login(String userName, String password) {
    logger.info("authorization method called");
    UserDetails userDetails = manager.loadUserByUsername(userName);
    if (userDetails == null) {
      logger.debug("authorization failed, login or password is incorrect");
      return false;
    }
    logger.info("authorization was successful");
    return encoder.matches(password, userDetails.getPassword());
  }

  /**
   * метод регистрации пользователя
   * */
  @Override
  public boolean register(RegisterReq registerReq, String role) {
    logger.info("user registration method called");
    if (manager.userExists(registerReq.getUsername())) {
      logger.debug("the user already exists");
      return false;
    }
    Role newRole = checkRole(role);
    myUserDetailsService.setPassword(encoder.encode(registerReq.getPassword()));
    myUserDetailsService.setUsername(registerReq.getUsername());
    myUserDetailsService.setRoles(new HashSet<>(List.of(newRole)));
    myUserDetailsService.setPhone(registerReq.getPhone());
    myUserDetailsService.setLastName(registerReq.getLastName());
    myUserDetailsService.setFirstName(registerReq.getFirstName());
    manager.createUser(myUserDetailsService);

    return true;
  }

  /**
   * проверка наличия роли в БД
   * @return Role
   * */
  private Role checkRole(String role) {
    Role role1 = roleRepository.findByName(role);
    if (role1 == null) {
      Role newRole = new Role(role);
      roleRepository.save(newRole);
      role1 = newRole;
    }
    return role1;
  }


}
