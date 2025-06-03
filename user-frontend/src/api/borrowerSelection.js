import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/borrower-selection';

export const confirmLoanSelection = async (leadsId, data, otp) => {
  const response = await axios.post(
    `${BASE_URL}/confirm-loan-selection/${leadsId}?otp=${otp}`,
    data
  );
  return response.data;
};

export const getLoanConfirmation = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/${leadsId}`);
  return response.data;
};

export const generateLoanConfirmationOtp = async (leadsId) => {
  const response = await axios.post(`${BASE_URL}/generate-confirmation-otp/${leadsId}`);
  return response.data;
};

export const resendLoanConfirmationOtp = async (leadsId) => {
  const response = await axios.post(`${BASE_URL}/resend-confirmation-otp/${leadsId}`);
  return response.data;
};
