package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    List<Booking> getAll();

    Booking getById(long id);

    Booking partialUpdate(long id, Booking booking);

    void remove(long id);
}
