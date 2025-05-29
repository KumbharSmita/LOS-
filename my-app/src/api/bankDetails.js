import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/bank';

export const submitBankDetails = async (leadId, bankDetails) => {
  const response = await axios.post(`${BASE_URL}/${leadId}/bank-details`, bankDetails);
  return response.data;
};
