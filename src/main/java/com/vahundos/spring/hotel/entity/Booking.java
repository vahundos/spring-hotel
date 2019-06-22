package com.vahundos.spring.hotel.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vahundos.spring.hotel.view.Views;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String personName;

    private Integer adultsCount;

    private Integer childrenCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;
}
