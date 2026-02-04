package com.revworkforce.service.impl;

import com.revworkforce.dao.IUserDao;
import com.revworkforce.model.Users;
import com.revworkforce.service.IAuthService;

public class AuthServiceImpl implements IAuthService {

    private IUserDao userDao;

    public AuthServiceImpl(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Users login(String email, String password) {

        Users user = userDao.getUserForLogin(email);

        if (user == null) {
            throw new RuntimeException("Invalid email");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        if (!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        userDao.updateLastLogin(user.getUserId());
        return user;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        Users user = userDao.getUserById(userId);

        if (!user.getPasswordHash().equals(oldPassword)) {
            throw new RuntimeException("Old password incorrect");
        }

        return userDao.updatePassword(userId, newPassword);
    }
}
