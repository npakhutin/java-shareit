package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddBookingDto {
    @NotBlank(message = "Должно быть задано начало периода бронирования")
    String start;
    @NotBlank(message = "Должен быть задан конец периода бронирования")
    String end;
    @NotNull(message = "Необходимо указать id бронируемой вещи")
    private Long itemId;
}
