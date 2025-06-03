import api from './axios';

export const logAgentActivity = async (agent_id, actionType, actionDetails) => {
  const response = await api.post('/agent/activity/log', { agent_id, actionType, actionDetails });
  return response.data;
};

export const fetchLogsByAgentId = async (agent_id) => {
  const response = await api.get(`/agent/activity/agentlog/${agent_id}`);
  return response.data;
};

export const fetchAllLogs = async (agent_id) => {
  const url = agent_id ? `/agent/activity/all-logs?agent_id=${agent_id}` : '/agent/activity/all-logs';
  const response = await api.get(url);
  return response.data;
};