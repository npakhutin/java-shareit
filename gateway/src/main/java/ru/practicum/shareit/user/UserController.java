package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.AddUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return userClient.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        return userClient.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid AddUserRequestDto addUserRequestDto) {
        return userClient.addNewUser(addUserRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserById(@RequestBody @Valid UpdateUserRequestDto userDto, @PathVariable Long id) {
        if (userDto.allFieldsAreEmpty()) {
            throw new IllegalArgumentException("Должно быть задано хотя бы одно изменяемое поле");
        }
        return userClient.updateUserById(id, userDto);
    }

/*
    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid AddItemRequestDto newItemDto) {
        return userClient.addNewItem(userId, newItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return userClient.findItemById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userClient.findAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "") String text) {
        return userClient.searchItem(userId, text);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("id") Long itemId,
                                             @RequestBody @Valid UpdateItemRequestDto itemDto) {
        if (itemDto.isEmpty()) {
            throw new IllegalArgumentException("Должно быть задано хотя бы одно изменяемое поле");
        }
        return userClient.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("id") Long itemId,
                                             @RequestBody @Valid AddCommentDto commentDto) {
        return userClient.addNewComment(userId, itemId, commentDto);
    }
*/
}
