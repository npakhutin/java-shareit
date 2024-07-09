package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithRelatedDataRow;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId, Sort sort);

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

    @Query("""
            select new ru.practicum.shareit.item.model.ItemWithRelatedDataRow(i, b1, b2, c)
            from Item as i
            JOIN User as u on i.owner = u
            left JOIN Booking as b1 on b1.item = i
                and b1.end = (select max(bb.end)
                                    from Booking as bb
                                    where bb.item = i
                                        and bb.end <= ?#{T(java.time.LocalDateTime).now()}
                                        and bb.status <> 'REJECTED')
            left JOIN Booking as b2 on b2.item = i
                and b2.start = (select min(bb.start)
                                    from Booking as bb
                                    where bb.item = i
                                        and bb.start >= ?#{T(java.time.LocalDateTime).now()}
                                        and bb.status <> 'REJECTED')
            left JOIN Comment as c on c.item = i
            where u.id = ?1
            order by i.id, b1.start, b2.start, c.created
            """)
    List<ItemWithRelatedDataRow> findAllWithRelatedDataByOwner(Long owner);

    @Query("""
            select new ru.practicum.shareit.item.dto.CommentDto(c.id, c.text, a.name, c.created)
            from Comment as c
            join c.author as a
            where c.item = ?1
            """)
    List<CommentDto> findCommentDtosForItem(Item item);
}
