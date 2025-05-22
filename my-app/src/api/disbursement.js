import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/disbursements';

// Disburse loan
export const disburseLoan = async (payload) => {
  const response = await axios.post(`${BASE_URL}/disburse-loan`, payload);
  return response.data;
};

// Get disbursement by lead ID
export const getDisbursementByLeadId = async (leadId) => {
  const response = await axios.get(`${BASE_URL}/${leadId}`);
  return response.data;
};
