package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setup() {
        filmController = new FilmController();
    }

    @Test
    void addFilm_ValidFilm_Success() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2000, Month.JANUARY, 1));
        film.setDuration(120);

        Film addedFilm = filmController.addFilm(film);

        assertNotNull(addedFilm);
        assertEquals("Test Film", addedFilm.getName());
        assertEquals(1, addedFilm.getId());
    }

    @Test
    void addFilm_EmptyName_ThrowsValidationException() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, Month.JANUARY, 1));
        film.setDuration(100);

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Название фильма не может быть пустым", ex.getMessage());
    }

    @Test
    void addFilm_DescriptionTooLong_ThrowsValidationException() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("A".repeat(201)); // строка длиной 201 символ
        film.setReleaseDate(LocalDate.of(2000, Month.JANUARY, 1));
        film.setDuration(100);

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void addFilm_ReleaseDateBefore1895_ThrowsValidationException() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1800, Month.JANUARY, 1));
        film.setDuration(100);

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", ex.getMessage());
    }

    @Test
    void addFilm_NegativeDuration_ThrowsValidationException() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(2000, Month.JANUARY, 1));
        film.setDuration(-10);

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Продолжительность фильма должна быть положительной", ex.getMessage());
    }
}