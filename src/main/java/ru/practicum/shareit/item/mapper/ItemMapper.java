package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingBasicInfoDto;
import ru.practicum.shareit.item.dto.AddItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRelatedDataDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithRelatedDataRow;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(),
                           item.getName(),
                           item.getDescription(),
                           item.getAvailable(),
                           item.getOwner().getId());
    }

    public static ItemWithRelatedDataDto mapToItemWithRelatedDataDto(Item item,
                                                                     BookingBasicInfoDto lastBooking,
                                                                     BookingBasicInfoDto nextBooking,
                                                                     List<CommentDto> comments) {
        return new ItemWithRelatedDataDto(item.getId(),
                                          item.getName(),
                                          item.getDescription(),
                                          item.getAvailable(),
                                          item.getOwner().getId(),
                                          lastBooking,
                                          nextBooking,
                                          comments);
    }

    public static Item mapToItem(User owner, AddItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), owner);
    }

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(mapToItemDto(item));
        }
        return itemDtos;
    }

    public static List<ItemWithRelatedDataDto> mapToItemWithRelatedDataDto(List<ItemWithRelatedDataRow> rows) {
        Map<Item, ItemWithRelatedDataDto> mapDto = new HashMap<>();
        for (ItemWithRelatedDataRow row : rows) {
            ItemWithRelatedDataDto dto;
            if (!mapDto.containsKey(row.getItem())) {
                dto =
                        mapToItemWithRelatedDataDto(row.getItem(),
                                                    Optional.ofNullable(row.getLastBooking())
                                                            .map(BookingMapper::mapToBookingBasicInfoDto)
                                                            .orElse(null),
                                                    Optional.ofNullable(row.getNextBooking())
                                                            .map(BookingMapper::mapToBookingBasicInfoDto)
                                                            .orElse(null),
                                                    new ArrayList<>());
                mapDto.put(row.getItem(), dto);
            } else {
                dto = mapDto.get(row.getItem());
            }
            if (row.getComment() != null) {
                dto.getComments().add(mapToCommentDto(row.getComment()));
            }
        }
        return List.copyOf(mapDto.values());
    }

    private static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }
}
