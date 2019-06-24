package com.vahundos.spring.hotel.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vahundos.spring.hotel.entity.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.vahundos.spring.hotel.TestData.*;
import static com.vahundos.spring.hotel.TestUtils.getFixtureContent;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookingControllerIT {

    private static final String BOOKING_ID_PARAM = "idBooking";

    private static final String BOOKING_PATH_PARAM_URI = String.format("/{%s}", BOOKING_ID_PARAM);

    private static final String INVALID_BOOKING_ID = "qwe_=";

    private static final String BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME = "booking-partial-update-request.json";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/booking";
    }

    // GET BY ID

    @Test
    public void testGetById_WhenIdExists_ShouldReturnSuccessResponse() {
        Booking actual = validatableResponseForGettingById(BOOKING1.getId(), OK.value())
                .extract().body().as(Booking.class);

        assertThat(actual).isEqualTo(BOOKING1);
    }

    @Test
    public void testGetById_WhenIdNotExists_ShouldReturnSuccessResponse() {
        validatableResponseForGettingById(Long.MAX_VALUE, OK.value(), "{}");
    }

    @Test
    public void testGetById_WhenIdIsInvalid_ShouldReturnBadRequestResponse() {
        validatableResponseForGettingById(INVALID_BOOKING_ID, BAD_REQUEST.value(), "");
    }

    // GET ALL

    @Test
    public void testGetAll_ShouldReturnSuccessResponse() {
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
    public void testGetAll_WhenAcceptNotSupported_ShouldReturnBadRequest() {
        given()
                .accept(ContentType.HTML)
                .log().all()
                .get()

                .then()
                .log().all()
                .statusCode(BAD_REQUEST.value())
                .body(is(equalTo("")));
    }

    // CANCEL BOOKING

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCancelBooking_WhenIdExists_ShouldReturnSuccessResponse() {
        validatableResponseForCanceling(BOOKING1.getId(), OK);
    }

    @Test
    public void testCancelBooking_WhenIdIsInvalid_ShouldReturnBadRequestResponse() {
        validatableResponseForCanceling(INVALID_BOOKING_ID, BAD_REQUEST);
    }

    @Test
    public void testCancelBooking_WhenIdNotExists_ShouldReturnNotFoundResponse() {
        validatableResponseForCanceling(Long.MAX_VALUE, NOT_FOUND);
    }

    // CREATE BOOKING

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCreateBooking_WhenBodyValid_ShouldReturnSuccessResponse() throws JSONException {
        String requestBody = getFixtureContent("booking-creation-request.json");
        String responseBody = getFixtureContent("booking-creation-response.json", BOOKING3.getId() + 1);

        validatableResponseForCreation(requestBody, OK, responseBody);
    }

    @Test
    public void testCreateBooking_WhenBodyInvalid_ShouldReturnBadRequestResponse() throws JSONException {
        validatableResponseForCreation("person: \"name\"}", BAD_REQUEST, "");
    }

    @Test
    public void testCreateBooking_WhenBodyIsEmptyJsonObject_ShouldReturnBadRequestResponse() throws JSONException {
        validatableResponseForCreation("{}", BAD_REQUEST, "");
    }

    @Test
    public void testCreateBooking_WhenBodyContainsId_ShouldReturnBadRequestResponse() throws JSONException {
        String requestBody = getFixtureContent("invalid-booking-creation-request.json");

        validatableResponseForCreation(requestBody, BAD_REQUEST, "");
    }

    private void validatableResponseForGettingById(Object bookingId, int statusCode, String body) {
        ValidatableResponse validatableResponse = validatableResponseForGettingById(bookingId, statusCode);

        if (body != null) {
            validatableResponse.body(is(equalTo(body)));
        }
    }

    // PARTIAL UPDATE

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testPartialUpdate_WhenBodyValid_ShouldReturnSuccessResponse() {
        String expectedName = "Person";
        String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, expectedName);

        Booking actual = validatableResponseForPartialUpdate(BOOKING1.getId(), requestBody, OK)
                .extract().body().as(Booking.class);

        assertThat(actual).isEqualToIgnoringGivenFields(BOOKING1, "personName");
        assertThat(actual.getPersonName()).isEqualTo(expectedName);
    }

    @Test
    public void testPartialUpdate_WhenIdInvalid_ShouldReturnBadRequestResponse() {
        String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, "Person");

        validatableResponseForPartialUpdate(INVALID_BOOKING_ID, requestBody, BAD_REQUEST)
                .body(is(equalTo("")));
    }

    @Test
    public void testPartialUpdate_WhenBodyIsEmpty_ShouldReturnBadRequestResponse() {
        validatableResponseForPartialUpdate(BOOKING1.getId(), "", BAD_REQUEST)
                .body(is(equalTo("")));
    }

    @Test
    public void testPartialUpdate_WhenIdNotExists_ShouldReturnNotFoundResponse() {
        String requestBody = getFixtureContent(BOOKING_PARTIAL_UPDATE_REQUEST_FILE_NAME, "Person");

        validatableResponseForPartialUpdate(Long.MAX_VALUE, requestBody, NOT_FOUND)
                .body(is(equalTo("")));
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
