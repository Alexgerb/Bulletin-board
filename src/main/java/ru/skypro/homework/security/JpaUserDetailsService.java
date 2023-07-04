package ru.skypro.homework.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.provisioning.UserDetailsManager;


import ru.skypro.homework.model.entity.Role;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.UserProfileRepository;

import java.util.Set;

public class JpaUserDetailsService implements UserDetailsManager {

    private final UserProfileRepository userProfileRepository;
    private final MyUserDetailsService myUserDetailsService;

    public JpaUserDetailsService(UserProfileRepository userProfileRepository, MyUserDetailsService myUserDetailsService) {
        this.userProfileRepository = userProfileRepository;
        this.myUserDetailsService = myUserDetailsService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userProfileRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        myUserDetailsService.setUsername(user.getUsername());
        myUserDetailsService.setPassword(user.getPassword());
        myUserDetailsService.setRoles(user.getRoles());
        return myUserDetailsService;
    }

    @Override
    public void createUser(UserDetails user) {
        UserProfile userProfile = new UserProfile(user.getUsername(), user.getPassword(), (Set<Role>) user.getAuthorities());
        userProfileRepository.save(userProfile);
    }

    @Override
    public void updateUser(UserDetails user) {
        userProfileRepository.save((UserProfile) user);
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
