package com.vahundos.spring.hotel.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.service.BookingService;
import com.vahundos.spring.hotel.view.Views;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping(path = "/booking")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PutMapping(consumes = "application/json", produces = "application/json")
    @JsonView(Views.Id.class)
    public Booking create(@RequestBody Booking booking) {
        log.debug("create booking - {}", booking);
        return bookingService.create(booking);
    }

    @GetMapping(produces = "application/json")
    public List<Booking> getAll() {
        log.debug("getAll bookings");
        return bookingService.getAll();
    }

    @GetMapping(path = "/{idBooking}", produces = "application/json")
    public Booking getById(@PathVariable("idBooking") long bookingId) {
        log.debug("getById booking by id - {}", bookingId);
        return bookingService.getById(bookingId);
    }

    @DeleteMapping(path = "/{idBooking}")
    public void cancelBooking(@PathVariable("idBooking") long bookingId) {
        log.debug("cancelBooking by id - {}", bookingId);
        bookingService.remove(bookingId);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Booking updateBooking(@RequestBody Booking booking) {
        return null;
    }
}
