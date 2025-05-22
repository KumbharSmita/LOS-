import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/lead-assignments',
});

export const fetchAssignedLeads = async (agentId) => {
  try {
    const response = await axiosInstance.get(`/assigned-leads/${agentId}`);
    if (!Array.isArray(response.data)) {
      throw new Error('Invalid API response format');
    }
    return response.data;
  } catch (error) {
    console.error('Error fetching leads:', error);
    throw new Error(error.message);
  }
};

export const fetchAssignedLeadsByStatus = async (agentId, status) => {
  try {
    const response = await axiosInstance.get(`/assigned-leads/${agentId}/status`, {
      params: { status },
    });
    if (!Array.isArray(response.data)) {
      throw new Error('Invalid API response format');
    }
    return response.data;
  } catch (error) {
    console.error('Error fetching filtered leads:', error);
    throw new Error(error.message);
  }
};
