// src/api/leadStatusHistory.js
import api from './axios';

// Submit Lead Status
export const submitStatus = async (leadId, agentId, status) => {
  const data = { leads_id: leadId, agent_id: agentId, status: status };
  return api.post('/lead-status-history/save', data);
};

// Fetch Lead History for a specific lead
export const fetchLeadHistory = async (leadId) => {
 return api.get(`/lead-status-history/lead/${leadId}`);
};

// Fetch My History (for a specific agent)
export const fetchMyAgentHistory = async (agentId) => {
  return api.get(`/lead-status-history/agent/${agentId}`);
};


