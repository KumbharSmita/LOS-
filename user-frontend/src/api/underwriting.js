import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/underwriting',
});

axiosInstance.interceptors.request.use(
  (config) => {
    // Add token only for non-GET requests
    if (config.method !== 'get') {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const performUnderwriting = async (leadId, approvedAmount, rateOfInterest, tenureMonths) => {
  const response = await axiosInstance.post('/underwrite', {
    leadsId: leadId,
    approvedAmount,
    rateOfInterest,
    tenureMonths,
  });
  return response.data;
};

// Public GET: No token
export const fetchUnderwritingByLeadId = async (leadId) => {
  const response = await axiosInstance.get(`/${leadId}`);
  return response.data;
};

// Secured GET (still requires token)
export const fetchAllUnderwritingResults = async () => {
  const response = await axiosInstance.get('/all');
  return response.data;
};

export const fetchMyUnderwritingResults = async () => {
  const response = await axiosInstance.get('/my-underwriting');
  return response.data;
};
