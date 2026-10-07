package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * The FlightInfoResource unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoResourceTest {

    // FIXME - applicant to complete.
	@Mock
    private FlightInfoService flightInfoService;

    private FlightInfoResource flightInfoResource;

    @BeforeEach
    void setUp() {
    	flightInfoResource = new FlightInfoResource(flightInfoService);
    }

    @Test
    void shouldPassRequestedDateToServiceAndReturnFlights() throws Exception {
        LocalDate date = LocalDate.of(2026, 10, 6);
        Flight flight = new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.TUESDAY));
        when(flightInfoService.findFlightByDate(date))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(List.of(flight))));

        ResponseEntity<?> response = flightInfoResource.getResults(date).toCompletableFuture().get();

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody(), equalTo(List.of(flight)));
        verify(flightInfoService).findFlightByDate(date);
    }

    @Test
    void shouldReturnNoContentWhenThereAreNoFlights() throws Exception {
        LocalDate date = LocalDate.of(2026, 10, 7);
        when(flightInfoService.findFlightByDate(date))
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        ResponseEntity<?> response = flightInfoResource.getResults(date).toCompletableFuture().get();

        assertThat(response.getStatusCodeValue(), equalTo(204));
    }
}

