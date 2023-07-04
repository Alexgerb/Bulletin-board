package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.security.JpaUserDetailsService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;
    private final JpaUserDetailsService userDetailsService;
    private final ImageServiceImpl imageService;


    @Override
    public UserDto getMe(String username) {
        UserDto userDto = new UserDto();
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        userDto.setEmail(username);
        userDto.setPhone(userProfile.getPhone());
        userDto.setFirstName(userProfile.getFirstName());
        userDto.setLastName(userProfile.getLastName());
        userDto.setImage("/users/me/getAvatar");
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        userProfile.setFirstName(userDto.getFirstName());
        userProfile.setLastName(userDto.getLastName());
        userProfile.setPhone(userDto.getPhone());
        userProfileRepository.save(userProfile);
        return userDto;

    }

    @Override
    public Void updateUserImage(MultipartFile image, String username){
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        try {
            imageService.uploadImage(userProfile, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean changePassword(NewPassword newPassword, String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        if (!encoder.matches(newPassword.getNewPassword(), userProfile.getPassword())) {
            userProfile.setPassword(encoder.encode(newPassword.getNewPassword()));
            userProfileRepository.save(userProfile);
            return true;
        }
        return false;

    }

    @Override
    public String getAvatar(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        String path = userProfile.getAvatar().getFilePath();
        return path;
    }

    @Override
    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username);
    }


    @Override
    public byte[] getImage(String username){
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        return userProfile.getAvatar().getData();
    }

    @Override
    public byte[] getImageByte(UserProfile userProfile) {
        try {
            return Files.readAllBytes(Paths.get(userProfile.getAvatar().getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserProfile getUserById(Integer id) {
        return userProfileRepository.findById(id).orElse(new UserProfile());
    }


}
