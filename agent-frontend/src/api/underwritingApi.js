import axios from 'axios';

export const getLeadStatus = (email) => {
  return axios.post('http://localhost:8080/api/underwriting/lead-status', { email });
};
