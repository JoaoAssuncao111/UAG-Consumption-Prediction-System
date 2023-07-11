# UAG Prediction System API Documentation

This document provides an overview of the endpoints available in the UAG Prediction System API.

## Base URL
https://api.example.com


## Endpoints

### Get UAGs
- URL: `/uags`
- Method: GET
- Description: Retrieves the list of UAGs (Unmanned Aerial Vehicles).
- Response:
  - Status Code: 200 (OK)
  - Body: Array of UAG objects

### Get Reading
- URL: `/readings/{readingType}`
- Method: GET
- Description: Retrieves readings based on the given reading type and input parameters.
- Request Parameters:
  - `readingType` (Path Variable): Type of reading (e.g., temperature, humidity).
- Query Parameters (ModelAttribute):
  - `startDate` (String): Start date of the reading period.
  - `endDate` (String): End date of the reading period.
  - `location` (String): Location for which the readings are requested.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of reading objects

### Get Temperature and Consumption
- URL: `/temp-cons`
- Method: GET
- Description: Retrieves temperature and consumption readings based on the input parameters.
- Query Parameters (ModelAttribute):
  - `startDate` (String): Start date of the reading period.
  - `endDate` (String): End date of the reading period.
  - `location` (String): Location for which the readings are requested.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of temperature and consumption objects

### Execute Training
- URL: `/training`
- Method: PUT
- Description: Retrieves the training data based on the input parameters.
- Query Parameters (ModelAttribute):
  - `startDate` (String): Start date of the training period.
  - `endDate` (String): End date of the training period.
- Response:
  - Status Code: 200 (OK)
  - Body: Training data object

### Insert UAG
- URL: `/uags`
- Method: POST
- Description: Inserts a new UAG (Unmanned Aerial Vehicle) observation.
- Request Body:
  - `observation` (String): Observation details.
  - `name` (String): UAG name.
  - `distance` (String): Distance traveled.
  - `latitude` (String): Latitude coordinate.
  - `longitude` (String): Longitude coordinate.
- Response:
  - Status Code: 200 (OK)
  - Body: Result object

### Get UAG by Name
- URL: `/uags/{name}`
- Method: GET
- Description: Retrieves UAG information based on the given name.
- Request Parameters:
  - `name` (Path Variable): UAG name.
- Response:
  - Status Code: 200 (OK)
  - Body: UAG object


---

This API documentation provides details about the available endpoints and their functionalities. Refer to the individual endpoints for more information on request/response structures and parameters.
