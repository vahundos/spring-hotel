package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    List<Booking> getAll();

    Booking getById(long id);

    Booking update(long id, Booking booking);

    void cancel(long id);
}
