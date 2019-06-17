package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking create(Booking booking) {
        if (booking.getId() == null) {
            throw new RuntimeException("Booking.id should be null for creation");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking get(int id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    @Override
    public Booking update(Booking booking, int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }
}
