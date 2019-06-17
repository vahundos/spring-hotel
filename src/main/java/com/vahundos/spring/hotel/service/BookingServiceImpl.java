package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.exception.NotFoundException;
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
        if (booking.getId() != null) {
            throw new IllegalArgumentException(booking + " - id should be null for creation");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking get(int id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Can't find booking with id=%d", id))
        );
    }

    @Override
    public Booking update(Booking booking, int id) {
        if (!bookingRepository.existsById(id)) {
            throw new NotFoundException(String.format("Can't find booking with id=%d for UPDATE", id));
        }
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    @Override
    public void remove(int id) {
        bookingRepository.deleteById(id);
    }
}
