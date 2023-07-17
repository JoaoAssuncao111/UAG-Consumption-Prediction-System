# Use the official Node.js image as the base image
FROM node:18.6 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json to the container
COPY js/uag-prediction-system/package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the frontend code to the container
COPY js/uag-prediction-system/src ./src
COPY js/uag-prediction-system/public ./public

# Build the React app
RUN npm run build

# Use a lightweight web server to serve the production build
FROM nginx:alpine

# Copy the built React app from the previous stage
COPY --from=build /app/build /usr/share/nginx/html

# Expose the port on which the web server listens (default is 80)
EXPOSE 8081

# Start the web server with npm start
CMD ["npm", "start"]
