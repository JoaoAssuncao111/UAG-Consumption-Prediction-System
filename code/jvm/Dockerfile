# Stage 1: Build the Spring backend
FROM openjdk:17 AS backend

# Set the working directory inside the container
WORKDIR /app

# Copy the build output (JAR file) from the Gradle build
COPY build/libs/uag-consumption-prediction-system.jar /app/uag-consumption-prediction-system.jar

# Stage 2: Install Python and its dependencies
FROM python:3.8 AS python

# Set the working directory inside the container
WORKDIR /app

# Copy the python scripts and requirements.txt
COPY python_scripts /app/python_scripts
COPY galp_file/galp_data.txt /app/galp_data.txt
COPY requirements.txt /app/requirements.txt

# Install Python dependencies
RUN pip3 install --no-cache-dir -r requirements.txt

# Stage 3: Final image for running the backend
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the first stage
COPY --from=backend /app/uag-consumption-prediction-system.jar /app/uag-consumption-prediction-system.jar

# Copy the installed Python dependencies from the second stage
COPY --from=python /usr/local/lib/python3.8/site-packages /usr/local/lib/python3.8/site-packages

# Expose the port on which the Spring application runs (replace with your actual port)
EXPOSE 8080

# Set the entrypoint command to run the Spring Boot application
CMD ["java", "-jar", "uag-consumption-prediction-system.jar"]
