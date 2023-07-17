# Use the official Node.js image as the base image
FROM node:18.6

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all files from the current directory into the container's working directory
COPY . .

# Build the React application
RUN npm run build

# Expose the port on which your React application will run (you may adjust the port as needed)
EXPOSE 8081

# Set the command to run your React application
CMD ["npm", "start"]