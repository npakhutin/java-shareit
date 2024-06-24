package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.exception.UnknownItemException;
import ru.practicum.shareit.user.exception.UnknownUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Item save(Item item) {
        item.setId(++idCounter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new UnknownItemException("Не найдена вещь id = " + itemId);
        }
        return item;
    }

    @Override
    public Item update(Item updatedItem) {
        Item item = items.get(updatedItem.getId());
        if (item == null) {
            throw new UnknownUserException("Не найдена вещь id = " + updatedItem.getId());
        }

        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }

        return item;
    }

    @Override
    public List<Item> getByOwnerId(Long ownerId) {
        return items.values()
                        .stream()
                        .filter(i -> Objects.equals(ownerId, i.getOwnerId()))
                        .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAll() {
        return List.copyOf(items.values());
    }
}
