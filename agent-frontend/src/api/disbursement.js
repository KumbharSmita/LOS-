import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/disbursements';

// Generate OTP - expects { leadsId }
export const generateOtp = (leadsId) => {
  return axios.post(`${BASE_URL}/generate-otp`, { leadsId });
};

// Verify OTP - expects { leadsId, otpValue }
export const verifyOtp = (leadsId, otpValue) => {
  return axios.post(`${BASE_URL}/verify-otp`, { leadsId, otpValue });
};

// Resend OTP - expects { leadsId }
export const resendOtp = (leadsId) => {
  return axios.post(`${BASE_URL}/resend-otp`, { leadsId });
};

// Disburse Loan - expects full disbursement payload (DisbursementsDTO shape)
export const disburseLoan = async (payload) => {
  const response = await axios.post(`${BASE_URL}/disburse-loan`, payload);
  return response.data;
};

// Get Disbursement by Lead ID
export const getDisbursementByLeadId = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/${leadsId}`);
  return response.data;
};
