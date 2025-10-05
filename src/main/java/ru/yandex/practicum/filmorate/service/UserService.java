package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        log.info("Добавление пользователя: {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Обновление пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        log.info("Получение пользователя по id: {}", id);
        return userStorage.getUserById(id);
    }

    public Collection<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        log.info("Добавление друга {} для пользователя {}", friendId, userId);
        if (userId == friendId) {
            throw new ValidationException("Пользователь не может добавить себя в друзья");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add((long) friendId);
        friend.getFriends().add((long) userId);
    }

    public void removeFriend(int userId, int friendId) {
        log.info("Удаление друга {} у пользователя {}", friendId, userId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove((long) friendId);
        friend.getFriends().remove((long) userId);
    }

    public Collection<User> getFriends(int userId) {
        log.info("Получение друзей пользователя {}", userId);
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream()
                .map(id -> userStorage.getUserById(id.intValue()))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(int userId, int otherId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherId);
        User user = userStorage.getUserById(userId);
        User other = userStorage.getUserById(otherId);
        Set<Long> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(other.getFriends());
        return commonFriends.stream()
                .map(id -> userStorage.getUserById(id.intValue()))
                .collect(Collectors.toList());
    }
}