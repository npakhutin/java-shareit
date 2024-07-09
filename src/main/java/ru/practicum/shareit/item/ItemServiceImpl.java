package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.IncorrectBookingException;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.UnknownItemException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.AddItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRelatedDataDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithRelatedDataRow;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addNewItem(Long ownerId, AddItemDto itemDto) {
        User
                user =
                userRepository.findById(ownerId)
                        .orElseThrow(() -> new UnknownUserException("Не найден пользователь id = " + ownerId));
        Item item = ItemMapper.mapToItem(user, itemDto);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItemById(Long ownerId, Long itemId, UpdateItemDto updateItemDto) {
        Item
                item =
                itemRepository.findById(itemId)
                        .orElseThrow(() -> new UnknownItemException("Не найдена вещь id = " + itemId));

        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new IncorrectOwnerException("Указан неправильный владелец вещи");
        }

        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(item);
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public ItemWithRelatedDataDto findById(Long userId, Long id) {
        Item
                item =
                itemRepository.findById(id).orElseThrow(() -> new UnknownItemException("Не найдена вещь id = " + id));

        Booking lastBooking = null;
        Booking nextBooking = null;
        if (Objects.equals(item.getOwner().getId(), userId)) {
            Page<Booking> bookings;
            bookings = bookingRepository.getLastBookings(item, PageRequest.of(0, 1, Sort.by("start").descending()));
            if (bookings.hasContent()) {
                lastBooking = bookings.getContent().getFirst();
            }
            bookings = bookingRepository.getNextBookings(item, PageRequest.of(0, 1, Sort.by("start").ascending()));
            if (bookings.hasContent()) {
                nextBooking = bookings.getContent().getFirst();
            }
        }

        List<CommentDto> comments = itemRepository.findCommentDtosForItem(item);

        return ItemMapper.mapToItemWithRelatedDataDto(item,
                                                      Optional.ofNullable(lastBooking)
                                                           .map(BookingMapper::mapToBookingBasicInfoDto)
                                                           .orElse(null),
                                                      Optional.ofNullable(nextBooking)
                                                           .map(BookingMapper::mapToBookingBasicInfoDto)
                                                           .orElse(null),
                                                      comments);
    }

    @Override
    public List<ItemWithRelatedDataDto> findAllWithRelatedDataByOwner(Long ownerId) {

        List<ItemWithRelatedDataRow> itemsData = itemRepository.findAllWithRelatedDataByOwner(ownerId);

        return ItemMapper.mapToItemWithRelatedDataDto(itemsData);
    }

    @Override
    public List<ItemDto> search(Long ownerId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return ItemMapper.mapToItemDto(itemRepository.findAvailableItemsByNameAndDescriptionIgnoreCase(text));
    }

    @Override
    @Transactional
    public CommentDto addNewComment(Long userId, Long itemId, AddCommentDto commentDto) {
        List<Booking> bookings = bookingRepository.findComletedByBookerAndItem(userId, itemId);
        if (bookings.isEmpty()) {
            throw new IncorrectBookingException("Комментарий может оставить только пользователь, ранее бронировавший вещь");
        }
        User author = bookings.getFirst().getBooker();
        Item item = bookings.getFirst().getItem();
        Comment comment = commentRepository.save(new Comment(null, commentDto.getText(), item, author, LocalDateTime.now()));
        return CommentMapper.mapToCommentDto(comment);
    }
}
