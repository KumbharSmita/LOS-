import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/disbursements';

export const generateOtp = (leadsId) => {
  return axios.post(`${BASE_URL}/generate-otp`, { leadsId });
};

export const verifyOtp = (leadsId, otpValue) => {
  return axios.post(`${BASE_URL}/verify-otp`, { leadsId, otpValue });
};

export const resendOtp = (leadsId) => {
  return axios.post(`${BASE_URL}/resend-otp`, { leadsId });
};

export const disburseLoan = async (payload) => {
  const response = await axios.post(`${BASE_URL}/disburse-loan`, payload);
  return response.data;
};

export const getDisbursementByLeadId = async (leadId) => {
  const response = await axios.get(`${BASE_URL}/${leadId}`);
  return response.data;
};
