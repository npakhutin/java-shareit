package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addNewUser(UserDto userDto) {
        checkDuplicates(null, userDto);
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUserById(Long userId, UpdateUserDto updateUserDto) {
        UserDto userDto = UserMapper.mapToUserDto(userRepository.getById(userId));
        if (updateUserDto.getName() != null) {
            userDto.setName(updateUserDto.getName());
        }
        if (updateUserDto.getEmail() != null) {
            userDto.setEmail(updateUserDto.getEmail());
        }
        checkDuplicates(userId, userDto);
        User user = userRepository.update(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.mapToUserDto(userRepository.getById(id));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private void checkDuplicates(Long userId, UserDto userDto) {
        Optional<User> user = userRepository.getByEmail(userDto.getEmail());
        if (user.isPresent() && !Objects.equals(user.get().getId(), userId)) {
            throw new DuplicateEmailException("Уже существует пользователь с email: " + userDto.getEmail());
        }
    }
}
