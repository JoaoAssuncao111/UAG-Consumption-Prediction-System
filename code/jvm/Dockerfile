# Use the official OpenJDK image as the base image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the JVM folder containing the Spring backend code
COPY code/jvm/ /app

# Copy the Python scripts and data.txt file into the container
COPY python_scripts/ /app/python_scripts
COPY text_file/data.txt /app/data.txt

# Install required packages to run Python scripts
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    pip3 install pandas  # Assuming pandas is needed for the Python scripts

# Expose the port on which the Spring application runs (replace with your actual port)
EXPOSE 8080

# Set environment variables for database connection (replace with your actual database credentials)
ENV DB_URL=jdbc:postgresql://database:5432/postgres
ENV DB_USER=postgres
ENV DB_PASSWORD=joaopedro123

# Change the working directory to the root of the Spring application
WORKDIR /app/src/main/kotlin/uagpredictionsystem

# Build the Spring backend (you may need to adjust the build command based on your build tool)
RUN ./gradlew build

# Start the Spring Boot application
CMD ["java", "-jar", "/app/build/libs/uag-consumption-prediction-system.jar"]
