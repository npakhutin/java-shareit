package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.AddBookingDto;
import ru.practicum.shareit.booking.dto.BookingBasicInfoDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemBasicInfoDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserIdDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingMapper {
    public static Booking mapToBooking(User booker, Item item, AddBookingDto newBookingDto) {
        LocalDateTime startDate = LocalDateTime.parse(newBookingDto.getStart());
        LocalDateTime endDate = LocalDateTime.parse(newBookingDto.getEnd());
        if (!(startDate.isAfter(LocalDateTime.now()) &&
                endDate.isAfter(LocalDateTime.now()) &&
                startDate.isBefore(endDate))) {
            throw new BadRequestException("Задан неправильный период бронирования");
        }

        return new Booking(null, startDate, endDate, BookingStatus.WAITING, booker, item);
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        Item item = booking.getItem();
        return new BookingDto(booking.getId(),
                              booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME),
                              booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME),
                              booking.getStatus(),
                              new UserIdDto(booking.getBooker().getId()),
                              new ItemBasicInfoDto(item.getId(), item.getName()));
    }

    public static BookingBasicInfoDto mapToBookingBasicInfoDto(Booking booking) {
        return new BookingBasicInfoDto(Optional.ofNullable(booking).map(Booking::getId).orElse(null),
                                       Optional.ofNullable(booking).map((b) -> b.getBooker().getId()).orElse(null));
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDtos.add(mapToBookingDto(booking));
        }
        return bookingDtos;
    }
}
