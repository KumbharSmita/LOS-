import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/documents';

export const uploadDocument = async (formData) => {
  const response = await axios.post(`${BASE_URL}/upload`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return response.data;
};

export const fetchDocumentsByLeadId = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/lead/${leadsId}`);
  return response.data;
};
