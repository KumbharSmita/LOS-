import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/repayment-schedule';

export const getRepaymentSchedule = async (leadsId) => {
  const response = await axios.get(`${BASE_URL}/${leadsId}`);
  return response.data;
};
