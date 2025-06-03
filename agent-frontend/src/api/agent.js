import axios from 'axios';

export const registerAgent = async (data) => {
  return await axios.post('http://localhost:8080/api/agent/register', data);
};

export const loginAgent = async (data) => {
  return await axios.post('http://localhost:8080/api/agent/login', data);
};