package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.exception.InvalidEntityException;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.repository.BookingRepository;
import com.vahundos.spring.hotel.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
            throw new InvalidEntityException(booking + " - id should be null for creation");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getById(long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id)
        );
    }

    @Override
    public Booking partialUpdate(long id, Booking booking) {
        Booking entity = this.getById(id);
        BeanUtils.copyProperties(booking, entity, CommonUtils.getNullPropertyNames(booking));

        return bookingRepository.save(entity);
    }

    @Override
    public void remove(long id) {
        if (!bookingRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }
}
