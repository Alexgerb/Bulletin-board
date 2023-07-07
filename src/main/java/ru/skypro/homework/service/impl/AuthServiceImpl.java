package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


  private final JpaUserDetailsService manager;

  private final PasswordEncoder encoder;

  private final RoleRepository roleRepository;

  private final MyUserDetailsService myUserDetailsService;

  @Override
  public boolean login(String userName, String password) {
    UserDetails userDetails = manager.loadUserByUsername(userName);
    if (userDetails == null) {
      return false;
    }
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq, String role) {
    if (manager.userExists(registerReq.getUsername())) {
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
