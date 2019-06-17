package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    List<Booking> getAll();

    Booking get(int id);

    Booking update(Booking booking, int id);

    void remove(int id);
}
