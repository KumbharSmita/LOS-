// src/components/LeadStatusHistory.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { submitStatus, fetchLeadHistory, fetchMyAgentHistory } from '../api/leadStatusHistory';

const TABS = {
  SUBMIT: 'Submit Status',
  MY_HISTORY: 'My Status History',
  LEAD_HISTORY: 'View Lead History',
  
};

const LeadStatusHistory = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState(TABS.SUBMIT);
  const [agentId, setAgentId] = useState(null);
  const [role, setRole] = useState('');
  const [message, setMessage] = useState('');
  const [status, setStatus] = useState('');
  const [leadId, setLeadId] = useState('');
  const [agentInputId, setAgentInputId] = useState('');
  const [leadHistory, setLeadHistory] = useState([]);
  const [agentHistory, setAgentHistory] = useState([]);

  // On mount, validate login and role
  useEffect(() => {
    const token = localStorage.getItem('token');
    const agent_id = localStorage.getItem('agent_id');
    const agent_role = localStorage.getItem('agent_role');

    if (!token || !agent_id || !agent_role) {
      setMessage('Unauthorized. Please login.');
      navigate('/agent-login');
      return;
    }

    setAgentId(agent_id);
    setRole(agent_role);
  }, [navigate]);

  const handleSubmitStatus = async (e) => {
  e.preventDefault();
  console.log("Trying to submit:", { leadId, agentId, status });

  try {
    await submitStatus(leadId, agentId, status);
    setMessage('Status submitted successfully!');
  } catch (error) {
    console.error("Submit error:", error);
    setMessage(error?.response?.data || 'Error submitting status.');
  }
};
  const fetchLeadHistoryData = async () => {
    try {
      const res = await fetchLeadHistory(leadId);
      setLeadHistory(res.data);
      setMessage('');
    } catch (error) {
      console.error(error.response || error);
      setMessage('Error fetching lead history.');
    }
  };

  const fetchMyAgentHistoryData = async () => {
    try {
      const res = await fetchMyAgentHistory(agentId);
      setAgentHistory(res.data);
      setMessage('');
    } catch (error) {
      console.error(error.response || error);
      setMessage('Error fetching your status history.');
    }
  };

  
  const renderHistoryTable = (data, additionalHeaders = []) => {
    if (!data.length) return null;

    return (
      <table className="w-full mt-4 border text-sm">
        <thead className="bg-gray-200">
          <tr>
            {additionalHeaders.includes('Lead ID') && <th className="p-2">Lead ID</th>}
            {additionalHeaders.includes('Agent ID') && <th className="p-2">Agent ID</th>}
            <th className="p-2">Status</th>
            <th className="p-2">Timestamp</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={item.history_id}>
              {additionalHeaders.includes('Lead ID') && <td className="p-2">{item.leads_id}</td>}
              {additionalHeaders.includes('Agent ID') && <td className="p-2">{item.agent_id}</td>}
              <td className="p-2">{item.status}</td>
              <td className="p-2">{new Date(item.updated_at).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case TABS.SUBMIT:
        return (
          <form onSubmit={handleSubmitStatus} className="space-y-4">
            <input
              type="number"
              placeholder="Lead ID"
              value={leadId}
              onChange={(e) => setLeadId(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="text"
              placeholder="Status"
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
            <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded">
              Submit
            </button>
          </form>
        );

      case TABS.MY_HISTORY:
        return (
          <div>
            <button
              onClick={fetchMyAgentHistoryData}
              className="bg-purple-600 text-white px-4 py-2 rounded mb-2"
            >
              Fetch My History
            </button>
            {renderHistoryTable(agentHistory, ['Lead ID'])}
          </div>
        );

      case TABS.LEAD_HISTORY:
        return (
          <div>
            <input
              type="number"
              placeholder="Lead ID"
              value={leadId}
              onChange={(e) => setLeadId(e.target.value)}
              className="w-full p-2 border rounded mb-2"
            />
            <button
              onClick={fetchLeadHistoryData}
              className="bg-blue-600 text-white px-4 py-2 rounded"
            >
              Fetch Lead History
            </button>
            {renderHistoryTable(leadHistory, ['Status', 'Agent ID'])}
          </div>
        );

      

      default:
        return null;
    }
  };

  const renderTabs = () => {
    const tabsToShow = [TABS.SUBMIT, TABS.MY_HISTORY];

    if (role === 'SUPER_ADMIN') {
      tabsToShow.push(TABS.LEAD_HISTORY, TABS.AGENT_HISTORY);
    }

    return (
      <div className="flex flex-wrap gap-2 mb-6">
        {tabsToShow.map((tab) => (
          <button
            key={tab}
            className={`px-4 py-2 rounded ${activeTab === tab ? 'bg-blue-700 text-white' : 'bg-gray-200'}`}
            onClick={() => {
              setActiveTab(tab);
              setMessage('');
              setLeadHistory([]);
              setAgentHistory([]);
            }}
          >
            {tab}
          </button>
        ))}
      </div>
    );
  };

  return (
    <div className="p-6 max-w-4xl mx-auto bg-white shadow rounded">
      <h2 className="text-2xl font-bold text-center mb-4 text-blue-700">Welcome, {role}</h2>
      {message && <p className="text-red-600 mb-4">{message}</p>}
      {renderTabs()}
      {renderTabContent()}
    </div>
  );
};

export default LeadStatusHistory;
