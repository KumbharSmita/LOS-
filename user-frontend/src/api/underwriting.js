import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/underwriting',
});


axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // adjust key if needed
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const performUnderwriting = async (leadId, approvedAmount, rateOfInterest, tenureMonths) => {
  const response = await axiosInstance.post('/underwrite', {
    leadsId: leadId,
    approvedAmount: approvedAmount,
    rateOfInterest: rateOfInterest,
    tenureMonths: tenureMonths,
  });
  return response.data;
};
// Get all underwriting results
export const fetchAllUnderwritingResults = async () => {
  const response = await axiosInstance.get('/all');
  return response.data;
};

// Get underwriting result by leadId
export const fetchUnderwritingByLeadId = async (leadId) => {
  const response = await axiosInstance.get(`/${leadId}`);
  return response.data;
};
