package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingBasicInfoDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithRelatedDataDto {
    private Long id;
    @NotBlank(message = "Наименование не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Необходимо задать доступность")
    private Boolean available;
    private Long ownerId;
    private BookingBasicInfoDto lastBooking;
    private BookingBasicInfoDto nextBooking;
    private List<CommentDto> comments;
}
