import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/leads',
});

export const createLead = async (lead) => {
  const res = await API.post('/create', lead);
  return res.data; // Assumes { leads_id } in response
};

export const verifyOtp = async (leads_id, otp_value) => {
  const res = await API.post('/verify-otp', { leads_id, otp_value });
  return res.data;
};

export const resendOtp = async (email) => {
  const res = await API.post('/resend-otp', { email });
  return res.data;
};
