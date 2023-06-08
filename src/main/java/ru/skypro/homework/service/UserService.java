package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UserDto;

public interface UserService {
    void setPassword(NewPassword newPassword);
    UserDto getMe();
    UserDto updateUser();
    Void updateUserImage();
}
