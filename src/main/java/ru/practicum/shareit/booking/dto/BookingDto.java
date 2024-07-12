package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBasicInfoDto;
import ru.practicum.shareit.user.dto.UserIdDto;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private String start;
    private String end;
    private BookingStatus status;
    private UserIdDto booker;
    private ItemBasicInfoDto item;
}
