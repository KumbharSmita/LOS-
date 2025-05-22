import axios from 'axios';


const BASE_URL = 'http://localhost:8080/api';

// Registers a new user (POST /api/users/register)
export const registerUser = (userData) => {
  return axios.post(`${BASE_URL}/users/register`, userData);
};

// Logs in a user (POST /api/user-login/login)
export const loginUser = (credentials) => {
  return axios.post(`${BASE_URL}/user-login/login`, credentials);
};

// Fetch user by email (GET /api/user-login/get-by-email?email=...)
export const getUserByEmail = (email) => {
  return axios.get(`${BASE_URL}/user-login/get-by-email`, {
    params: { email },
  });
};
