# Virgin Atlantic ~ Flight Information Display

### Changes made
- Fixed FlightInfoServiceImpl
*uses the requested LocalDate
*Converts it to DayOfweek
*Filters flights based on operating days
*Sorts cronologically by departure time
*supports any year

- Fixed FlightInfoResource
* Uses the date supplied in the URL
instead of LocalDate.now()
*Uses Spring's ISO LocalDate conversion

-configured the existing CSV

- added service unit tests
- added resource unit tests

### Build the Project
In Eclipse,

Right click project - > Run As -> Maven Build.

In the goals, enter spring-boot:run

### Test the Application
http://localhost:8080/back-end-test/2026-10-07/results

### Response
[{"departureTime":"10:15","destination":"Orlando","iata":"MCO","flightNo":"VS027","days":["WEDNESDAY"]},{"departureTime":"10:35","destination":"Las Vegas","iata":"LAS","flightNo":"VS043","days":["MONDAY","TUESDAY","WEDNESDAY"]},{"departureTime":"11:05","destination":"Barbados","iata":"BGI","flightNo":"VS029","days":["SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"]},{"departureTime":"11:45","destination":"Orlando","iata":"MCO","flightNo":"VS049","days":["WEDNESDAY"]},{"departureTime":"12:25","destination":"Montego Bay","iata":"MBJ","flightNo":"VS065","days":["WEDNESDAY"]},{"departureTime":"13:00","destination":"Orlando","iata":"MCO","flightNo":"VS015","days":["SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"]},{"departureTime":"15:35","destination":"Las Vegas","iata":"LAS","flightNo":"VS044","days":["SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"]}]
