package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId);
        itemDto.setOwnerId(user.getId());
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItemById(Long userId, Long itemId, UpdateItemDto updateItemDto) {
        ItemDto itemDto = ItemMapper.mapToItemDto(itemRepository.getById(itemId));
        if (!Objects.equals(itemDto.getOwnerId(), userId)) {
            throw new IncorrectOwnerException("Указан неправильный владелец вещи");
        }

        if (updateItemDto.getName() != null) {
            itemDto.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            itemDto.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null) {
            itemDto.setAvailable(updateItemDto.getAvailable());
        }

        Item updatedItem = itemRepository.update(ItemMapper.mapToItem(itemDto));
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public ItemDto getById(Long id) {
        return ItemMapper.mapToItemDto(itemRepository.getById(id));
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemRepository.getByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return itemRepository.getAll()
                .stream()
                .filter(item -> !text.isEmpty() &&
                        item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }
}
