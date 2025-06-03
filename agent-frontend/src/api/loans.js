import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/loans';

export const processLoan = async (leadsId) => {
  const response = await axios.post(`${BASE_URL}/process-loan/${leadsId}`);
  return response.data;
};

export const getEmiSchedule = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/emi-schedule/${leadsId}`);
  return response.data;
};
