# Stage 1: Build the Spring backend
FROM openjdk:17.0.2 AS backend

# Set the working directory inside the container
WORKDIR /app

# Copy the build output (JAR file) from the Gradle build
COPY ./build/libs/uag-consumption-prediction-system.jar uag-consumption-prediction-system.jar

# Stage 2: Install Python dependencies and set up the Python environment
FROM python:3.11.4-slim-buster AS python

# Install the required system packages, including the missing libbz2 library
RUN apt-get update && apt-get install -y libbz2-dev

# Set the working directory inside the container
WORKDIR /app

# Copy the python scripts and requirements.txt
COPY python_scripts/ /app/python_scripts/
COPY galp_file/galp_data.txt /app/galp_data.txt
COPY requirements.txt /app/requirements.txt

# Install Python dependencies
RUN pip3 install --no-cache-dir -r /app/requirements.txt

# Stage 3: Final image for running the backend
FROM openjdk:17.0.2

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the first stage
COPY --from=backend /app/uag-consumption-prediction-system.jar uag-consumption-prediction-system.jar

# Copy the installed Python dependencies and libraries from the second stage
COPY --from=python /usr/local /usr/local

# Copy the Python scripts from the second stage
COPY --from=python /app/python_scripts /app/python_scripts

# Expose the port on which the Spring application runs (replace with your actual port)
EXPOSE 8080

# Set the entrypoint command to run the Spring Boot application
CMD ["java", "-jar", "uag-consumption-prediction-system.jar"]
