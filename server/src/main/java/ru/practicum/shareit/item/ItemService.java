package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.AddItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRelatedDataDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long ownerId, AddItemDto item);

    ItemDto updateItemById(Long ownerId, Long itemId, UpdateItemDto updateItemDto);

    ItemWithRelatedDataDto findById(Long userId, Long id);

    List<ItemWithRelatedDataDto> findAllWithRelatedDataByOwner(Long ownerId);

    List<ItemDto> search(Long ownerId, String text);

    CommentDto addNewComment(Long userId, Long itemId, AddCommentDto commentDto);
}
