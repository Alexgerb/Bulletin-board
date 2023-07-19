package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;
import ru.skypro.homework.repository.UserProfileRepository;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;
    private final ImageServiceImpl imageService;
    private final MyUserDetailsService myUserDetailsService;


    @Override
    public UserDto getMe() {
        UserDto userDto = new UserDto();
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        userDto.setEmail(myUserDetailsService.getUsername());
        userDto.setPhone(userProfile.getPhone());
        userDto.setFirstName(userProfile.getFirstName());
        userDto.setLastName(userProfile.getLastName());
        userDto.setImage("/users/"+userProfile.getId()+"/getAvatar");
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        userProfile.setFirstName(userDto.getFirstName());
        userProfile.setLastName(userDto.getLastName());
        userProfile.setPhone(userDto.getPhone());
        userProfileRepository.save(userProfile);
        return userDto;

    }

    @Override
    public Void updateUserImage(MultipartFile image ){
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
        try {
            imageService.uploadImage(userProfile, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean changePassword(NewPassword newPassword) {
        UserProfile userProfile = userProfileRepository.findByUsername(myUserDetailsService.getUsername());
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
        String path = userProfile.getAvatar().getFilePath();;
        return path;
    }

    @Override
    public UserProfile getUserProfile( ) {
        return userProfileRepository.findByUsername(myUserDetailsService.getUsername());
    }


    @Override
    public UserProfile getUserById(Integer id) {
        return userProfileRepository.findById(id).orElseThrow();
    }


}
