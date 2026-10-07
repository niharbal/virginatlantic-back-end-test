package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The FlightInfoServiceImpl unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoServiceImplTest {

    // FIX added
	 @Mock
	 private FlightInfoRepository flightInfoRepository;

	 @InjectMocks
	 private FlightInfoServiceImpl flightInfoService;
	 
	 @Test
	    void shouldReturnTuesdayFlightsInChronologicalOrder() throws Exception {
	        Flight late = flight("VS999", LocalTime.of(12, 0), DayOfWeek.TUESDAY);
	        Flight early = flight("VS001", LocalTime.of(9, 0), DayOfWeek.TUESDAY);
	        Flight otherDay = flight("VS888", LocalTime.of(8, 0), DayOfWeek.MONDAY);

	        when(flightInfoRepository.findAll()).thenReturn(
	                CompletableFuture.completedFuture(Optional.of(List.of(late, otherDay, early))));

	        Optional<List<Flight>> result = flightInfoService
	                .findFlightByDate(LocalDate.of(2026, 10, 6))
	                .toCompletableFuture()
	                .get();

	        assertThat(result.isPresent(), equalTo(true));
	        assertThat(result.get(), hasSize(2));
	        assertThat(result.get(), contains(early, late));
	    }

	    @Test
	    void shouldWorkForAnyYearBecauseOnlyDayOfWeekDeterminesSchedule() throws Exception {
	        Flight tuesdayFlight = flight("VS001", LocalTime.of(9, 0), DayOfWeek.TUESDAY);
	        when(flightInfoRepository.findAll()).thenReturn(
	                CompletableFuture.completedFuture(Optional.of(List.of(tuesdayFlight))));

	        Optional<List<Flight>> result = flightInfoService
	                .findFlightByDate(LocalDate.of(2099, 12, 29))
	                .toCompletableFuture()
	                .get();

	        assertThat(result.isPresent(), equalTo(true));
	        assertThat(result.get().get(0).flightNo(), equalTo("VS001"));
	    }

	    @Test
	    void shouldReturnEmptyWhenNoFlightOperatesOnRequestedDay() throws Exception {
	        Flight mondayFlight = flight("VS001", LocalTime.of(9, 0), DayOfWeek.MONDAY);
	        when(flightInfoRepository.findAll()).thenReturn(
	                CompletableFuture.completedFuture(Optional.of(List.of(mondayFlight))));

	        Optional<List<Flight>> result = flightInfoService
	                .findFlightByDate(LocalDate.of(2026, 10, 6))
	                .toCompletableFuture()
	                .get();

	        assertThat(result, equalTo(Optional.empty()));
	    }

	    @Test
	    void shouldReturnEmptyWhenRepositoryHasNoData() throws Exception {
	        when(flightInfoRepository.findAll()).thenReturn(
	                CompletableFuture.completedFuture(Optional.empty()));

	        Optional<List<Flight>> result = flightInfoService
	                .findFlightByDate(LocalDate.of(2026, 10, 6))
	                .toCompletableFuture()
	                .get();

	        assertThat(result, equalTo(Optional.empty()));
	    }

	    private Flight flight(String number, LocalTime time, DayOfWeek day) {
	        return new Flight(time, "Destination", "XXX", number, List.of(day));
	    }
}