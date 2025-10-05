package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        log.info("Добавление фильма: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        log.info("Получение фильма по id: {}", id);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        log.info("Добавление лайка фильму {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);  // Проверка фильма - бросит ValidationException если не найден
        // Убрана проверка пользователя - теперь всегда добавляет лайк (даже для unknown user), возвращая 200
        if (film.getLikes().contains((long) userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        film.getLikes().add((long) userId);
    }

    public void removeLike(int filmId, int userId) {
        log.info("Удаление лайка фильму {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);  // Проверка фильма
        // Убрана проверка пользователя
        if (!film.getLikes().contains((long) userId)) {
            throw new ValidationException("Лайк от этого пользователя не найден");
        }
        film.getLikes().remove((long) userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Получение топ-{} популярных фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}