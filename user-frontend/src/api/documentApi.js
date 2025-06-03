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

export const downloadDocumentById = async (documentId) => {
  const response = await axios.get(`${BASE_URL}/download/${documentId}`, {
    responseType: 'blob',  
  });
  return response;
};

export const requestReupload = async (leadsId, documentType) => {
  const response = await axios.post(
    `${BASE_URL}/request-reupload`,
    null,
    { params: { leadsId, documentType } }
  );
  return response.data;
};
