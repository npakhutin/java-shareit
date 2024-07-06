package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private Long idCounter = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> existingEmails = new HashSet<>();

    @Override
    public User getById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UnknownUserException("Не найден пользователь id = " + id);
        }
        return user;
    }

    @Override
    public User save(User user) {
        checkDuplicates(user);

        user.setId(++idCounter);
        users.put(user.getId(), user);
        existingEmails.add(user.getEmail());

        return user;
    }

    private void checkDuplicates(User user) {
        if (existingEmails.contains(user.getEmail())) {
            throw new DuplicateEmailException("Уже существует пользователь с email: " + user.getEmail());
        }
    }

    @Override
    public User update(User updatedUser) {
        checkDuplicates(updatedUser);

        User user = users.get(updatedUser.getId());
        if (user == null) {
            throw new UnknownUserException("Не найден пользователь id = " + updatedUser.getId());
        }

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingEmails.remove(user.getEmail());
            user.setEmail(updatedUser.getEmail());
            existingEmails.add(user.getEmail());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void deleteById(Long id) {
        User user = users.remove(id);
        existingEmails.remove(user.getEmail());
    }
}
