package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingBasicInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.AddItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRelatedDataDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static List<ItemWithRelatedDataDto> mapToItemWithRelatedDataDto(List<Item> items,
                                                                           List<Booking> lastBookings,
                                                                           List<Booking> nextBookings,
                                                                           List<Comment> comments) {
        Map<Item, Booking> lastBookingsMap = lastBookings.stream()
                .collect(Collectors.toMap(Booking::getItem, booking -> booking, (a, b) -> b));
        Map<Item, Booking> nextBookingsMap = nextBookings.stream()
                .collect(Collectors.toMap(Booking::getItem, booking -> booking, (a, b) -> b));
        Map<Item, List<Comment>> commentsMap = new HashMap<>();
        comments.forEach(comment -> commentsMap.getOrDefault(comment.getItem(), new ArrayList<>()).add(comment));

        List<ItemWithRelatedDataDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(mapToItemWithRelatedDataDto(item,
                                                     Optional.ofNullable(lastBookingsMap.get(item))
                                                             .map(BookingMapper::mapToBookingBasicInfoDto)
                                                             .orElse(null),
                                                     Optional.ofNullable(nextBookingsMap.get(item))
                                                             .map(BookingMapper::mapToBookingBasicInfoDto)
                                                             .orElse(null),
                                                     Optional.ofNullable(commentsMap.get(item))
                                                             .map(CommentMapper::mapToCommentDto)
                                                             .orElse(new ArrayList<>())));
        }
        return itemDtos;
    }
}
