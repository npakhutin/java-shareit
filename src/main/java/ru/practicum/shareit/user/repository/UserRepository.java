package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User getById(Long id);

    User save(User user);

    Optional<User> getByEmail(String email);

    User update(User updatedUser);

    List<User> getAll();

    void deleteById(Long id);
}
