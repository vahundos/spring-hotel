package com.vahundos.spring.hotel.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.service.BookingService;
import com.vahundos.spring.hotel.util.Views;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping(path = "/booking")
@Slf4j
public class BookingController {

    private static final String ID_BOOKING_PATH = "/{idBooking}";

    private final BookingService bookingService;

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @JsonView(Views.Id.class)
    public Booking create(@Valid @RequestBody Booking booking) {
        log.debug("create booking - {}", booking);
        return bookingService.create(booking);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Booking> getAll() {
        log.debug("getAll bookings");
        return bookingService.getAll();
    }

    @GetMapping(path = ID_BOOKING_PATH, produces = APPLICATION_JSON_VALUE)
    public Booking getById(@PathVariable("idBooking") long id) {
        log.debug("getById booking by id - {}", id);
        try {
            return bookingService.getById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
    }

    @DeleteMapping(path = ID_BOOKING_PATH)
    public void cancelBooking(@PathVariable("idBooking") long id) {
        log.debug("cancelBooking by id - {}", id);
        try {
            bookingService.remove(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = ID_BOOKING_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Booking partialBookingUpdate(@PathVariable("idBooking") long id, @RequestBody Booking booking) {
        log.debug("partialBookingUpdate with id - {} and body - {}", id, booking);
        return bookingService.partialUpdate(id, booking);
    }
}
