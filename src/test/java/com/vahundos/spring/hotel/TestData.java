package com.vahundos.spring.hotel;

import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.entity.NumberOfGuests;
import com.vahundos.spring.hotel.entity.RoomType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vahundos.spring.hotel.entity.RoomType.STANDART;
import static com.vahundos.spring.hotel.entity.RoomType.SUITE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static final Booking BOOKING1 = Booking.builder()
            .id(1L)
            .checkInDate(LocalDate.of(2019, 6, 22))
            .checkOutDate(LocalDate.of(2019, 6, 25))
            .personName("Mykola")
            .roomType(STANDART)
            .numberOfGuests(new NumberOfGuests(2, 3))
            .build();

    public static final Booking BOOKING2 = Booking.builder()
            .id(2L)
            .checkInDate(LocalDate.of(2019, 6, 15))
            .checkOutDate(LocalDate.of(2019, 6, 26))
            .personName("Halyna")
            .roomType(STANDART)
            .numberOfGuests(new NumberOfGuests(2, 0))
            .build();

    public static final Booking BOOKING3 = Booking.builder()
            .id(3L)
            .checkInDate(LocalDate.of(2019, 6, 1))
            .checkOutDate(LocalDate.of(2019, 6, 30))
            .personName("Maksym M.")
            .roomType(SUITE)
            .numberOfGuests(new NumberOfGuests(1, 0))
            .build();

    public static final List<Booking> ALL_BOOKINGS = Collections.unmodifiableList(new ArrayList<Booking>() {{
        add(BOOKING1);
        add(BOOKING2);
        add(BOOKING3);
    }});

    public static Booking getValidBooking() {
        return Booking.builder()
                .checkInDate(LocalDate.now())
                .checkOutDate(LocalDate.now())
                .numberOfGuests(new NumberOfGuests(1, 1))
                .personName("Person")
                .roomType(RoomType.STANDART)
                .build();
    }
}
