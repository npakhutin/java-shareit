package ru.practicum.shareit.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum BookingStatus {
        WAITING, APPROVED, REJECTED
    }

    public enum BookingStateForSearching {
        ALL, WAITING, APPROVED, REJECTED, CURRENT, FUTURE, PAST
    }
}
