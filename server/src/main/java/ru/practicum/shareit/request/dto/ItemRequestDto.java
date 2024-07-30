package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemBasicInfoDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    Long id;
    private String description;
    private String requesterName;
    private LocalDateTime created;
    List<ItemBasicInfoDto> items;
}
