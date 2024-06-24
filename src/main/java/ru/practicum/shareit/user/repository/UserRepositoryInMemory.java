package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UnknownUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private Long idCounter = 0L;
    private final Map<Long, User> users = new HashMap<>();

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
        user.setId(++idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        User user = users.values().stream().filter(u -> email.equals(u.getEmail())).findAny().orElse(null);

        return Optional.ofNullable(user);
    }

    @Override
    public User update(User updatedUser) {
        User user = users.get(updatedUser.getId());
        if (user == null) {
            throw new UnknownUserException("Не найден пользователь id = " + updatedUser.getId());
        }
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }
}
