package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwnerId());
    }

    public static Item mapToItem(ItemDto itemDto) {

        return new Item(itemDto.getId(),
                        itemDto.getName(),
                        itemDto.getDescription(),
                        itemDto.getAvailable(),
                        itemDto.getOwnerId());
    }

}
