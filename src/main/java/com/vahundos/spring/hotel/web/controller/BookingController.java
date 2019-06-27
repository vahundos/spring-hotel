package com.vahundos.spring.hotel.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.service.BookingService;
import com.vahundos.spring.hotel.util.Views;
import com.vahundos.spring.hotel.web.CommonHttpException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Booking")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping(path = "/booking")
@Slf4j
public class BookingController {

    private static final String ID_BOOKING_PATH = "/{idBooking}";

    private static final String BAD_REQUEST = "Bad request";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String BOOKING_NOT_FOUND = "Booking not found";
    private static final String BOOKING_SUCCESSFULLY_RETRIEVED = "Booking successfully retrieved";

    private final BookingService bookingService;

    @ApiOperation(value = "Create new booking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Booking created successfully"),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR)
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @JsonView(Views.Id.class)
    public Booking create(@Valid @RequestBody Booking booking) {
        log.debug("create booking - {}", booking);
        return bookingService.create(booking);
    }

    @ApiOperation(value = "List of all bookings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Booking> getAll() {
        log.debug("getAll bookings");
        return bookingService.getAll();
    }

    @ApiOperation(value = "Get booking by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = BOOKING_SUCCESSFULLY_RETRIEVED),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR)
    })
    @GetMapping(path = ID_BOOKING_PATH, produces = APPLICATION_JSON_VALUE)
    public Booking getById(@PathVariable("idBooking") long id) {
        log.debug("getById booking by id - {}", id);
        try {
            return bookingService.getById(id);
        } catch (NotFoundException e) {
            throw new CommonHttpException(HttpStatus.OK, "{}");
        }
    }

    @ApiOperation(value = "Cancel booking by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = BOOKING_SUCCESSFULLY_RETRIEVED),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 404, message = BOOKING_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR)
    })
    @DeleteMapping(path = ID_BOOKING_PATH)
    public void cancel(@PathVariable("idBooking") long id) {
        log.debug("cancelBooking by id - {}", id);
        bookingService.cancel(id);
    }

    @ApiOperation(value = "Partial update of booking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = BOOKING_SUCCESSFULLY_RETRIEVED),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 404, message = BOOKING_NOT_FOUND),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR)
    })
    @PostMapping(path = ID_BOOKING_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Booking update(@PathVariable("idBooking") long id, @RequestBody Booking booking) {
        log.debug("partialBookingUpdate with id - {} and body - {}", id, booking);
        return bookingService.update(id, booking);
    }
}
