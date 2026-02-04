package com.revworkforce.service;

import com.revworkforce.dao.IUserDao;
import com.revworkforce.model.Users;
import com.revworkforce.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private IUserDao userDao;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= LOGIN =================

    @Test
    void login_shouldSucceed_whenCredentialsAreValid() {

        Users user = new Users();
        user.setUserId(1);
        user.setPasswordHash("password");
        user.setActive(true);

        when(userDao.getUserForLogin("test@mail.com")).thenReturn(user);

        Users result = authService.login("test@mail.com", "password");

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        verify(userDao).updateLastLogin(1);
    }

    @Test
    void login_shouldThrowException_whenEmailInvalid() {

        when(userDao.getUserForLogin("wrong@mail.com")).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("wrong@mail.com", "password")
        );

        assertEquals("Invalid email", ex.getMessage());
    }

    @Test
    void login_shouldThrowException_whenAccountDeactivated() {

        Users user = new Users();
        user.setActive(false);

        when(userDao.getUserForLogin("test@mail.com")).thenReturn(user);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("test@mail.com", "password")
        );

        assertEquals("Account is deactivated", ex.getMessage());
    }

    @Test
    void login_shouldThrowException_whenPasswordInvalid() {

        Users user = new Users();
        user.setPasswordHash("correct");
        user.setActive(true);

        when(userDao.getUserForLogin("test@mail.com")).thenReturn(user);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("test@mail.com", "wrong")
        );

        assertEquals("Invalid password", ex.getMessage());
    }

    // ================= CHANGE PASSWORD =================

    @Test
    void changePassword_shouldSucceed_whenOldPasswordMatches() {

        Users user = new Users();
        user.setPasswordHash("oldPass");

        when(userDao.getUserById(1)).thenReturn(user);
        when(userDao.updatePassword(1, "newPass")).thenReturn(true);

        boolean result =
                authService.changePassword(1, "oldPass", "newPass");

        assertTrue(result);
        verify(userDao).updatePassword(1, "newPass");
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordIncorrect() {

        Users user = new Users();
        user.setPasswordHash("correct");

        when(userDao.getUserById(1)).thenReturn(user);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.changePassword(1, "wrong", "newPass")
        );

        assertEquals("Old password incorrect", ex.getMessage());
    }
}
