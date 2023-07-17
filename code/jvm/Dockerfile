# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JVM folder containing the Spring backend code
COPY ./ /app

# Copy the Python scripts and data.txt file into the container
COPY ./python_scripts/ /app/python_scripts
COPY ./excel_file/galp_data.txt /app/galp_data.txt

# Build the Spring backend (you may need to adjust the build command based on your build tool)
RUN ./gradlew build

# Expose the port on which the Spring application runs (replace with your actual port)
EXPOSE 8080

# Set environment variables for database connection (replace with your actual database credentials)
ENV DB_URL=jdbc:postgresql://database:5432/postgres
ENV DB_USER=postgres
ENV DB_PASSWORD=joaopedro123

# Start the Spring Boot application
CMD ["java", "-jar", "/app/build/libs/uag-prediction-system.jar"]
