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
import static com.vahundos.spring.hotel.TestData.BOOKING1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingServiceImpl testObj;

    @BeforeEach
    public void beforeEach() {
        this.testObj = new BookingServiceImpl(bookingRepository);
    }

    @Test
    public void testCreate_ShouldPass() {
        Booking bookingToCreate = new Booking();
        BeanUtils.copyProperties(BOOKING1, bookingToCreate, "id");

        when(bookingRepository.save(bookingToCreate)).thenReturn(BOOKING1);

        Booking booking = testObj.create(bookingToCreate);

        assertThat(booking).isEqualTo(BOOKING1);
    }

    @Test
    public void testCreate_ShouldFail_WhenIdNotNull() {
        Assertions.assertThrows(InvalidEntityException.class, () -> {
            testObj.create(BOOKING1);
        });
    }

    @Test
    public void testGetAll_ShouldPass() {
        when(bookingRepository.findAll()).thenReturn(ALL_BOOKINGS);

        List<Booking> actual = testObj.getAll();
        assertThat(actual).isEqualTo(ALL_BOOKINGS);
    }

    @Test
    public void testGetById_ShouldPass() {
        when(bookingRepository.findById(BOOKING1.getId())).thenReturn(Optional.of(BOOKING1));

        Booking actual = testObj.getById(BOOKING1.getId());
        assertThat(actual).isEqualTo(BOOKING1);
    }

    @Test
    public void testGetById_ShouldFail_WhenIdDoesntExist() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> testObj.getById(Long.MAX_VALUE));
    }

    @Test
    public void testPartialUpdate_ShouldSuccess() {
        Booking entity = new Booking();
        BeanUtils.copyProperties(BOOKING1, entity);

        when(bookingRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Booking updatedEntity = new Booking();
        BeanUtils.copyProperties(BOOKING1, updatedEntity);
        updatedEntity.setPersonName("PersonName");

        when(bookingRepository.save(updatedEntity)).thenReturn(updatedEntity);

        Booking actual = testObj.partialUpdate(updatedEntity.getId(), updatedEntity);
        assertThat(actual).isEqualTo(updatedEntity);
    }

    @Test
    public void testRemove_ShouldPass() {
        when(bookingRepository.existsById(BOOKING1.getId())).thenReturn(true);
        testObj.remove(BOOKING1.getId());
    }

    @Test
    public void testRemove_ShouldFail_WhenEntityDoesntExist() {
        when(bookingRepository.existsById(Long.MAX_VALUE)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> testObj.remove(Long.MAX_VALUE));
    }

}