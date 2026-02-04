package com.revworkforce.service;

import com.revworkforce.model.Users;

public interface IAuthService {

    public Users login(String email, String password);

    public boolean changePassword(int userId, String oldPassword, String newPassword);
}
