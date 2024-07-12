package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId, Sort sort);

    Optional<Item> findByIdAndOwnerIdNot(Long id, Long ownerId);

    @Query("""
            select it
            from Item as it
            where it.available and (
               lower(it.name) like concat('%', lower(?1), '%')
               or
               lower(it.description) like concat('%', lower(?1), '%')
            )
            order by it.id
            """)
    List<Item> findAvailableItemsByNameAndDescriptionIgnoreCase(String searchText);
}
