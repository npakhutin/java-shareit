package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    User getById(Long id);

    User save(User user);

    User update(User updatedUser);

    List<User> getAll();

    void deleteById(Long id);
}
