package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addNewUser(UserDto userDto);

    UserDto updateUserById(Long userId, UpdateUserDto userDto);

    List<UserDto> findAll();

    UserDto findById(Long id);

    void deleteById(Long id);
}
