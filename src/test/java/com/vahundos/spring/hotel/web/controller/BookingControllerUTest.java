package com.vahundos.spring.hotel.web.controller;

import com.vahundos.spring.hotel.web.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.vahundos.spring.hotel.TestConstants.BOOKING_BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("WeakerAccess")
@ExtendWith(MockitoExtension.class)
public class BookingControllerUTest {

    private MockMvc mockMvc;

    @Mock
    private BookingController bookingController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                                      .setControllerAdvice(new GlobalExceptionHandler())
                                      .build();
    }

    @Test
    public void testHandlingException_WhenUnexpectedExceptionOccurs() throws Exception {
        Mockito.when(bookingController.getAll()).thenThrow(new RuntimeException("Unexpected exeception"));

        mockMvc.perform(get(BOOKING_BASE_URL))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(""));
    }
}
