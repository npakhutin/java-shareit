package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item getById(Long itemId);

    Item update(Item item);

    List<Item> getByOwnerId(Long ownerId);

    List<Item> getAll();
}
