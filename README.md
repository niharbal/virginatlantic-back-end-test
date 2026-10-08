# Virgin Atlantic - Flight Information Display

This project implements the Virgin Atlantic Flight Information Display coding exercise.

The application allows a user to provide **any valid calendar date, in any year**, and returns the flights operating on that day in **chronological order of departure time**.

## Requirements

The implementation follows the assessment requirements:

1. Flight data remains unchanged and is loaded from `src/main/resources/flights.csv`.
2. No additional Maven dependencies have been added.
3. The requested date is supplied through the REST endpoint.
4. Flights are selected using the day of week represented by the requested date.
5. Results are ordered by departure time.
6. `//FIXME - applicant to complete` gaps have been addressed.
7. Unit and integration tests are included.
8. Invalid URL dates are rejected by Spring's date conversion/validation rather than being silently accepted.

## How the flight schedule works

The CSV does not contain a calendar date. Instead, each row contains a departure time and one or more operating days. An `x` in a day column means that the flight operates on that day.

For example:

```text
09:00,Antigua,ANU,VS033,,,x,,,
```

means VS033 operates at 09:00 on Tuesday.

Therefore, a request such as:

```text
/2026-10-06/results
```

is evaluated as a **Tuesday** schedule. The year does not need to be hard-coded; the application uses `LocalDate` and its `DayOfWeek` value.

## API

### Get flights for a date

```http
GET /{date}/results
```

Example:

```http
GET /2026-10-06/results
Accept: application/json
```

The response contains flights operating on that date, sorted by departure time.

Example response shape:

```json
[
  {
    "departureTime": "09:00",
    "destination": "Antigua",
    "iata": "ANU",
    "flightNo": "VS033",
    "days": ["TUESDAY"]
  }
]
```

### Any year

The same day-of-week schedule can be requested for any year:

```text
GET /2026-10-06/results
GET /2030-10-08/results
```

The application calculates the day of week from each supplied `LocalDate`; it does not assume the current year.

## Invalid dates

Dates use the ISO-8601 format:

```text
YYYY-MM-DD
```

A valid URL date is accepted, for example:

```text
/2026-10-06/results
```

An impossible calendar date is rejected:

```text
/2026-02-30/results
```

This is tested through the real HTTP endpoint and results in `400 Bad Request`.

A URL that does not match the endpoint route, such as:

```text
/2026/10/06/results
```

does not match `/{date}/results` and results in `404 Not Found`.

## Architecture

The application keeps the existing separation of responsibilities:

```text
HTTP request
    |
    v
FlightInfoResource
    |
    v
FlightInfoService
    |
    v
FlightInfoRepository
    |
    v
flights.csv
```

### Resource

`FlightInfoResource` accepts the date from the URL and passes the resulting `LocalDate` to the service.

### Service

`FlightInfoServiceImpl`:

1. Gets the `DayOfWeek` from the requested `LocalDate`.
2. Retrieves the flight data from the repository.
3. Filters flights that operate on the requested day.
4. Sorts flights by `departureTime`.
5. Uses `flightNo` as a secondary ordering key to make equal departure times deterministic.

### Repository

The repository reads the supplied CSV data. The CSV is treated as the source of truth and is not modified by the application.

## Configuration

The CSV location is configurable through `application.properties`:

```properties
backend-test.data-source.csv-location=flights.csv
```

This keeps the application independent of a hard-coded Java data set and allows the source CSV to be replaced without changing the flight data in code.

## Testing

The project contains unit and integration tests.

### Unit tests

The unit tests cover areas including:

- CSV repository loading/parsing.
- Filtering flights for the requested day.
- Chronological ordering.
- Different calendar years.
- No matching flights.
- Service behaviour when the repository has no data.
- Resource/service interaction.

### Integration tests

`FlightInfoResourceIntegrationTest` starts the Spring application context and calls the real endpoint using the test infrastructure supplied by the project.

It verifies:

- The real endpoint returns `200 OK` for a valid date.
- URL date parsing works with ISO dates.
- Flights returned by the real endpoint are ordered chronologically.
- The requested date is actually used.
- Dates from different years can be processed.
- An impossible calendar date such as `2026-02-30` returns `400 Bad Request`.
- An invalid route format returns `404 Not Found`.

## Running the application

### Run tests

From the project root:

```bash
mvn clean test
```

### Start the application

```bash
mvn spring-boot:run
```

Then call:

```text
http://localhost:8080/2026-10-06/results