package com.vahundos.spring.hotel.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vahundos.spring.hotel.util.Views;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private Long id;

    @NotNull
    private String personName;

    @Embedded
    @NotNull
    @Valid
    private NumberOfGuests numberOfGuests;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomType roomType;
}
