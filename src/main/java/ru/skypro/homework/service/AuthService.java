package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.RegisterReq;

public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, String roleEnum);
}
