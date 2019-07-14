package com.vahundos.spring.hotel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vahundos.spring.hotel.TestData;
import com.vahundos.spring.hotel.entity.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.vahundos.spring.hotel.TestConstants.BOOKING_BASE_URL;
import static com.vahundos.spring.hotel.TestData.*;
import static com.vahundos.spring.hotel.TestUtils.getFixtureContent;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class BookingControllerIT {

    private static final String BOOKING_ID_PARAM = "idBooking";

    private static final String BOOKING_PATH_PARAM_URI = String.format("/{%s}", BOOKING_ID_PARAM);

    private static final String INVALID_BOOKING_ID = "qwe_=";

    private static final String BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME = "booking-partial-update-request.json";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = BOOKING_BASE_URL;
    }


    @Nested
    class GetByIdTest {

        @Test
        void testGetById_WhenIdExists_ShouldReturnSuccessResponse() {
            Booking actual = validatableResponseForGettingById(getBooking1().getId(), OK.value())
                    .extract().body().as(Booking.class);

            assertThat(actual).isEqualTo(getBooking1());
        }

        @Test
        void testGetById_WhenIdNotExists_ShouldReturnSuccessResponse() {
            validatableResponseForGettingById(Long.MAX_VALUE, OK.value(), "{}");
        }

        @Test
        void testGetById_WhenIdIsInvalid_ShouldReturnBadRequestResponse() {
            validatableResponseForGettingById(INVALID_BOOKING_ID, BAD_REQUEST.value(), "");
        }

        private void validatableResponseForGettingById(Object bookingId, int statusCode, String body) {
            ValidatableResponse validatableResponse = validatableResponseForGettingById(bookingId, statusCode);

            if (body != null) {
                validatableResponse.body(is(equalTo(body)));
            }
        }

        private ValidatableResponse validatableResponseForGettingById(Object bookingId, int statusCode) {
            return given()
                    .pathParam(BOOKING_ID_PARAM, bookingId)
                    .log().all()

                    .when()
                    .get(BOOKING_PATH_PARAM_URI)

                    .then()
                    .log().all()
                    .statusCode(statusCode);
        }
    }

    @Nested
    class GetAllTest {

        @Test
        void testGetAll_ShouldReturnSuccessResponse() {
            List<Booking> actual = given()
                    .get()

                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(OK.value())
                    .extract().jsonPath().getList(".", Booking.class);

            assertThat(actual).isEqualTo(ALL_BOOKINGS);
        }

        @Test
        void testGetAll_WhenAcceptNotSupported_ShouldReturnBadRequest() {
            given()
                    .accept(ContentType.HTML)
                    .log().all()
                    .get()

                    .then()
                    .log().all()
                    .statusCode(BAD_REQUEST.value())
                    .body(is(equalTo("")));
        }
    }

    @Nested
    @NestedSql
    class CancelTest {

        @Test
        void testCancel_WhenIdExists_ShouldReturnSuccessResponse() {
            validatableResponseForCanceling(getBooking1().getId(), OK);
        }

        @Test
        void testCancel_WhenIdIsInvalid_ShouldReturnBadRequestResponse() {
            validatableResponseForCanceling(INVALID_BOOKING_ID, BAD_REQUEST);
        }

        @Test
        void testCancel_WhenIdNotExists_ShouldReturnNotFoundResponse() {
            validatableResponseForCanceling(Long.MAX_VALUE, NOT_FOUND);
        }

        private void validatableResponseForCanceling(Object bookingId, HttpStatus httpStatus) {
            given()
                    .pathParam(BOOKING_ID_PARAM, bookingId)
                    .log().all()

                    .when()
                    .delete(BOOKING_PATH_PARAM_URI)

                    .then()
                    .log().all()
                    .statusCode(httpStatus.value())
                    .body(is(equalTo("")));
        }
    }

    @Nested
    @NestedSql
    class CreateTest {

        @Test
        void testCreate_WhenBodyValid_ShouldReturnSuccessResponse() throws JSONException {
            String responseBody = getFixtureContent("booking-creation-response.json", getBooking3().getId() + 1);

            validatableResponseForCreation(getObjAsJson(TestData.getValidBookingForCreation()), OK, responseBody);
        }

        @Test
        void testCreate_WhenBodyInvalid_ShouldReturnBadRequestResponse() throws JSONException {
            validatableResponseForCreation("person: \"name\"}", BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenBodyIsEmptyJsonObject_ShouldReturnBadRequestResponse() throws JSONException {
            validatableResponseForCreation("{}", BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenBodyContainsId_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.setId(10L);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenAdultsNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.getNumberOfGuests().setAdults(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenChildrenNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.getNumberOfGuests().setChildren(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenNumberOfGuestsNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.setNumberOfGuests(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenCheckInDateNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.setCheckInDate(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenCheckOutDateNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.setCheckOutDate(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenRoomTypeNull_ShouldReturnBadRequestResponse() throws JSONException {
            Booking requestBody = getValidBookingForCreation();
            requestBody.setRoomType(null);

            validatableResponseForCreation(getObjAsJson(requestBody), BAD_REQUEST, "");
        }

        @Test
        void testCreate_WhenDateFormatInvalid_ShouldReturnBadRequestResponse() throws JSONException {
            String requestBody = getFixtureContent("invalid-booking-creation-request.json");

            validatableResponseForCreation(requestBody, BAD_REQUEST, "");
        }

        private String getObjAsJson(Booking booking) {
            try {
                return objectMapper.writeValueAsString(booking);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        private void validatableResponseForCreation(String requestBody, HttpStatus httpStatus, String responseBody) throws JSONException {
            ValidatableResponse validatableResponse = given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .log().all()

                    .put()

                    .then()
                    .log().all()
                    .statusCode(httpStatus.value());

            if ("".equals(responseBody)) {
                validatableResponse
                        .body(is(equalTo(responseBody)));
            } else {
                String actualResponseBody = validatableResponse.extract().body().asString();
                JSONAssert.assertEquals(responseBody, actualResponseBody, false);
            }
        }
    }

    @Nested
    @NestedSql
    class UpdateTest {

        @Test
        void testPartialUpdate_WhenBodyValid_ShouldReturnSuccessResponse() {
            String expectedName = "Person";
            String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, expectedName);

            Booking actual = validatableResponseForPartialUpdate(getBooking1().getId(), requestBody, OK)
                    .extract().body().as(Booking.class);

            assertThat(actual).isEqualToIgnoringGivenFields(getBooking1(), "personName");
            assertThat(actual.getPersonName()).isEqualTo(expectedName);
        }

        @Test
        void testPartialUpdate_WhenIdInvalid_ShouldReturnBadRequestResponse() {
            String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, "Person");

            validatableResponseForPartialUpdate(INVALID_BOOKING_ID, requestBody, BAD_REQUEST)
                    .body(is(equalTo("")));
        }

        @Test
        void testPartialUpdate_WhenBodyIsEmpty_ShouldReturnBadRequestResponse() {
            validatableResponseForPartialUpdate(getBooking1().getId(), "", BAD_REQUEST)
                    .body(is(equalTo("")));
        }

        @Test
        void testPartialUpdate_WhenIdNotExists_ShouldReturnNotFoundResponse() {
            String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, "Person");

            validatableResponseForPartialUpdate(Long.MAX_VALUE, requestBody, NOT_FOUND)
                    .body(is(equalTo("")));
        }

        private ValidatableResponse validatableResponseForPartialUpdate(Object bookingId, String requestBody, HttpStatus httpStatus) {
            return given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .pathParam(BOOKING_ID_PARAM, bookingId)
                    .log().all()

                    .when()
                    .post(BOOKING_PATH_PARAM_URI)

                    .then()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .statusCode(httpStatus.value());
        }
    }
}
