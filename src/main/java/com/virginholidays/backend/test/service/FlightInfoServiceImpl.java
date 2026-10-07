package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import org.springframework.stereotype.Service;

/**
 * The service implementation of FlightInfoService
 *
 * @author Geoff Perks
 */
@Service
public class FlightInfoServiceImpl implements FlightInfoService {

    private final FlightInfoRepository flightInfoRepository;

    /**
     * The constructor
     *
     * @param flightInfoRepository the flightInfoRepository
     */
    public FlightInfoServiceImpl(FlightInfoRepository flightInfoRepository) {
        this.flightInfoRepository = flightInfoRepository;
    }

    @Override
    public CompletionStage<Optional<List<Flight>>> findFlightByDate(LocalDate outboundDate) {

        // FIX added
    	if (outboundDate == null) {
            return java.util.concurrent.CompletableFuture.completedFuture(Optional.empty());
        }

        return flightInfoRepository.findAll().thenApply(maybeFlights ->
                maybeFlights.flatMap(flights -> {
                    List<Flight> results = flights.stream()
                            .filter(flight -> flight.days().contains(outboundDate.getDayOfWeek()))
                            .sorted(Comparator.comparing(Flight::departureTime)
                                    .thenComparing(Flight::flightNo))
                            .toList();

                    return results.isEmpty() ? Optional.empty() : Optional.of(results);
                }));
    }
}
