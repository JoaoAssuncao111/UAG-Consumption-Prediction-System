# UAG Prediction System API Documentation

This document provides an overview of the endpoints available in the UAG Prediction System API.

## Endpoints

### Get UAGs
- URL: `/uags`
- Method: GET
- Description: Retrieves the list of UAGs.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of UAG objects

### Get Reading
- URL: `/readings/{readingType}`
- Method: GET
- Description: Retrieves readings based on the given reading type and input parameters.
- Request Parameters:
  - `readingType` (Path Variable): Type of reading (e.g., temperature, humidity, levels).
- Query Parameters:
  - `startDate` (String): Start date of the reading period.
  - `endDate` (String): End date of the reading period.
  - `location` (String): Location for which the readings are requested.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of reading object


### Get Temperature and Consumption
- URL: `/tempcons`
- Method: GET
- Description: Retrieves 5 day temperature prediction and 9 past days consumption readings based on the input parameters.
- Query Parameters:
  - `startDate` (String): Start date of the reading period.
  - `endDate` (String): End date of the reading period.
  - `location` (String): Location for which the readings are requested.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of temperature and consumption objects
  - 
### Get UAG by name
- URL: `/uag/{name}`
- Method: GET
- Description: Retrieves UAG details.
- Request Parameters:
  - `readingType` (Path Variable): Type of reading (e.g., temperature, humidity, levels).
- Response:
  - Status Code: 200 (OK)
  - Body: UAG Object
    
### Insert UAG
- URL: `/uags`
- Method: POST
- Description: Inserts a new UAG location.
- Request Body: 
  - `observation` (String): Observation details.
  - `name` (String): UAG name.
  - `distance` (String): Distance traveled.
  - `latitude` (String): Latitude coordinate.
  - `longitude` (String): Longitude coordinate.
- Response:
  - Status Code: 200 (OK)


### Execute Training
- URL: `/training`
- Method: PUT
- Description: Executes training algorithm on all UAG based on the input parameters.
- Query Parameters (ModelAttribute):
  - `startDate` (String): Start date of the training period.
  - `endDate` (String): End date of the training period.
- Response:
  - Status Code: 200 (OK)

### Get Deliveries
- URL: `/deliveries`
- Method: GET
- Description: Retrieves delivery data based on the input parameters.
- Query Parameters:
  - `startDate` (String): Start date of the delivery period.
  - `endDate` (String): End date of the delivery period.
  - `location` (String): Location for which deliveries are requested.
- Response:
  - Status Code: 200 (OK)
  - Body: Array of delivery objects

### Predict Consumptions
- URL: `/predict`
- Method: POST
- Description: Executes prediction algorithm based on the input parameters.
- Query Parameters (ModelAttribute):
  - `startDate` (String): Start date for the prediction period.
  - `endDate` (String): End date for the prediction period.
- Response:
  - Status Code: 200 (OK)

### Fetch IPMA Data
- URL: `/ipma`
- Method: POST
- Description: Fetches data from the IPMA API and stores it in the database.
- Response:
  - Status Code: 200 (OK)

### Get Prediction
- URL: `/prediction/{id}`
- Method: GET
- Description: Retrieves prediction details for a specific ID.
- Request Parameters:
  - `id` (Path Variable): Prediction ID.
- Response:
  - Status Code: 200 (OK)
  - Body: Prediction object

---

This API documentation provides details about the available endpoints and their functionalities. Refer to the individual endpoints for more information on request/response structures and parameters.
