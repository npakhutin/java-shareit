package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long userId, ItemDto item);

    ItemDto updateItemById(Long userId, Long itemId, UpdateItemDto updateItemDto);

    ItemDto getById(Long id);

    List<ItemDto> getAll(Long userId);

    List<ItemDto> search(Long userId, String text);
}
