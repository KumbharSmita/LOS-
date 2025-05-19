// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Change to match your Spring Boot base path
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
