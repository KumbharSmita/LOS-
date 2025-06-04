import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/leads',
});

export const createLead = async (lead) => {
  const res = await API.post('/create', lead);
  return res.data;
};


export const verifyOtp = async (leadsId, otpValue) => {
  console.log('API verifyOtp called with:', { leadsId, otpValue });
  const res = await API.post('/verify-otp', { leadsId, otpValue });
  return res.data;
};

export const resendOtp = async (email) => {
  const res = await API.post('/resend-otp', { email });
  return res.data;
};
export const getCreditScore = async (leadsId) => {
  const res = await API.get(`/${leadsId}/credit-score`);
  return res.data;
};
