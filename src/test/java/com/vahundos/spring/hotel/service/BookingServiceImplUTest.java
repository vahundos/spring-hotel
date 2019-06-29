package com.vahundos.spring.hotel.service;

import com.vahundos.spring.hotel.entity.Booking;
import com.vahundos.spring.hotel.exception.InvalidEntityException;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

import static com.vahundos.spring.hotel.TestData.ALL_BOOKINGS;
import static com.vahundos.spring.hotel.TestData.getBooking1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingServiceImpl testObj;

    @BeforeEach
    void beforeEach() {
        this.testObj = new BookingServiceImpl(bookingRepository);
    }

    @Test
    void testCreate_ShouldPass() {
        Booking bookingToCreate = new Booking();
        BeanUtils.copyProperties(getBooking1(), bookingToCreate, "id");

        when(bookingRepository.save(bookingToCreate)).thenReturn(getBooking1());

        Booking booking = testObj.create(bookingToCreate);

        assertThat(booking).isEqualTo(getBooking1());
    }

    @Test
    void testCreate_ShouldFail_WhenIdNotNull() {
        Assertions.assertThrows(InvalidEntityException.class, () -> testObj.create(getBooking1()));
    }

    @Test
    void testGetAll_ShouldPass() {
        when(bookingRepository.findAll()).thenReturn(ALL_BOOKINGS);

        List<Booking> actual = testObj.getAll();
        assertThat(actual).isEqualTo(ALL_BOOKINGS);
    }

    @Test
    void testGetById_ShouldPass() {
        when(bookingRepository.findById(getBooking1().getId())).thenReturn(Optional.of(getBooking1()));

        Booking actual = testObj.getById(getBooking1().getId());
        assertThat(actual).isEqualTo(getBooking1());
    }

    @Test
    void testGetById_ShouldFail_WhenIdDoesntExist() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> testObj.getById(Long.MAX_VALUE));
    }

    @Test
    void testUpdate_ShouldSuccess() {
        Long id = getBooking1().getId();
        when(bookingRepository.findById(id)).thenReturn(Optional.of(getBooking1()));

        String personName = "PersonName";

        Booking updatedEntity = getBooking1();
        updatedEntity.setId(null);
        updatedEntity.setPersonName(personName);

        Booking savedEntity = getBooking1();
        savedEntity.setPersonName(personName);

        when(bookingRepository.save(any())).thenReturn(savedEntity);

        Booking actual = testObj.update(id, updatedEntity);
        assertThat(actual).isEqualTo(savedEntity);
    }

    @Test
    void testUpdate_ShouldFail_WhenIdNotNull() {
        Assertions.assertThrows(InvalidEntityException.class, () -> testObj.update(getBooking1().getId(), getBooking1()));
    }

    @Test
    void testRemove_ShouldPass() {
        when(bookingRepository.existsById(getBooking1().getId())).thenReturn(true);
        testObj.cancel(getBooking1().getId());
    }

    @Test
    void testRemove_ShouldFail_WhenEntityDoesntExist() {
        when(bookingRepository.existsById(Long.MAX_VALUE)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> testObj.cancel(Long.MAX_VALUE));
    }

}