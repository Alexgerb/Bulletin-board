package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;

import java.io.IOException;

public interface UserService {
    UserDto getMe(String username);
    UserDto updateUser(UserDto updateProfileDto, String username);
    Void updateUserImage(MultipartFile image, String username);
    boolean changePassword(NewPassword newPassword, String username);
    String getAvatar(String username);
    UserProfile getUserProfile(String username);
    UserProfile getUserById(Integer id);
}
