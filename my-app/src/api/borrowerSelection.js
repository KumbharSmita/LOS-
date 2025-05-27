import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/borrower-selection';

export const confirmLoanSelection = async (leadsId, data) => {
  const response = await axios.post(`${BASE_URL}/confirm-loan-selection/${leadsId}`, data);
  return response.data;
};

export const getLoanConfirmation = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/${leadsId}`);
  return response.data;
};
