package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setup() {
        userController = new UserController();
    }

    @Test
    void createUser_ValidUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("validLogin", createdUser.getLogin());
        assertEquals("Test User", createdUser.getName());
        assertEquals(1, createdUser.getId());
    }

    @Test
    void createUser_EmailWithoutAt_ThrowsValidationException() {
        User user = new User();
        user.setEmail("invalidemail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Email не может быть пустым и должен содержать '@'", ex.getMessage());
    }

    @Test
    void createUser_LoginWithSpaces_ThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", ex.getMessage());
    }

    @Test
    void createUser_BirthdayInFuture_ThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException ex = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
    }

    @Test
    void createUser_NameIsBlank_NameSetToLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setName("  ");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertEquals("login", createdUser.getName());
    }
}