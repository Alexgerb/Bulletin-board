package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserProfile;

public interface UserService {
    UserDto getMe();
    UserDto updateUser(UserDto updateProfileDto );
    Void updateUserImage(MultipartFile image );
    boolean changePassword(NewPassword newPassword);
    String getAvatar(String username);
    UserProfile getUserProfile();
    UserProfile getUserById(Integer id);
}
