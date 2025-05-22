import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/underwriting',
});

export const performUnderwriting = async (leadId, approvedAmount) => {
  const response = await axiosInstance.post('/underwrite', {
    leadsId: leadId,
    approvedAmount: approvedAmount,
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
