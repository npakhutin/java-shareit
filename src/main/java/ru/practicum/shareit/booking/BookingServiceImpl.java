package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.Booking.BookingStateForSearching;
import ru.practicum.shareit.booking.Booking.BookingStatus;
import ru.practicum.shareit.exception.IncorrectBookingException;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.UnknownBookingException;
import ru.practicum.shareit.exception.UnknownItemException;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto addNewBooking(Long bookerId, AddBookingDto newBookingDto) {
        Item
                item =
                itemRepository.findById(newBookingDto.getItemId())
                        .orElseThrow(() -> new UnknownItemException("Не найдена вещь id = " +
                                                                            newBookingDto.getItemId()));
        if (Objects.equals(item.getOwner().getId(), bookerId)) {
            throw new UnknownItemException("Не найдена вещь с id = " +
                                                   newBookingDto.getItemId() +
                                                   ", которую мог бы забронировать пользователь с id = " +
                                                   bookerId);
        }
        if (!item.getAvailable()) {
            throw new IncorrectBookingException("Вещь с id = " + item.getId() + " недоступна");
        }

        User
                booker =
                userRepository.findById(bookerId)
                        .orElseThrow(() -> new UnknownUserException("Не найден пользователь id = " + bookerId));

        Booking booking = BookingMapper.mapToBooking(booker, item, newBookingDto);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking
                booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new UnknownBookingException("Не найдено бронирование с id = " + bookingId));

        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() == newStatus) {
            throw new IncorrectBookingException("Передан неправильный новый статус бронирования");
        }
        Item item = booking.getItem();
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new IncorrectOwnerException("Указан неправильный владелец вещи");
        }

        booking.setStatus(newStatus);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findById(Long userId, Long bookingId) {
        Booking
                booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new UnknownBookingException("Не найдено бронирование с id = " + bookingId));
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
            !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new UnknownBookingException("Просматривать бронирование может только инициатор или владелец вещи");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String stateString) {
        BookingStateForSearching state = getBookingStateForSearching(stateString);
        return BookingMapper.mapToBookingDto(bookingRepository.findByBookerIdAndState(userId, state.name()));
    }

    private BookingStateForSearching getBookingStateForSearching(String stateString) {
        BookingStateForSearching state;
        try {
            state = BookingStateForSearching.valueOf(stateString.toUpperCase());
        } catch (Exception e) {
            throw new IncorrectBookingException("Unknown state: " + stateString);
        }
        return state;
    }

    @Override
    public List<BookingDto> getBookingsByItemsOwner(Long ownerId, String stateString) {
        BookingStateForSearching state = getBookingStateForSearching(stateString);
        List<BookingDto>
                result =
                BookingMapper.mapToBookingDto(bookingRepository.findByOwnerIdAndState(ownerId, state.name()));
        if (result.isEmpty()) {
            throw new UnknownBookingException("Не найдено бронирований вещей");
        }
        return result;
    }
}
