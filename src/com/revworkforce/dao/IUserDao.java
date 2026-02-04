package com.revworkforce.dao;

import com.revworkforce.model.Users;

import java.util.List;

public interface IUserDao {

    int createUser(Users user);

    Users getUserByEmail(String email);

    Users getUserById(int userId);

    boolean updateUserStatus(int userId, boolean isActive);

    boolean updateLastLogin(int userId);

    Users getUserForLogin(String email);

    boolean updatePassword(int userId, String passwordHash);

    List<Users> getAllActiveUsers();
}

