import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { logAgentActivity, fetchLogsByAgentId, fetchAllLogs } from '../api/activityLog';

function AgentActivityLog() {
  const navigate = useNavigate();

  const [agentId, setAgentId] = useState('');
  const [actionType, setActionType] = useState('');
  const [actionDetails, setActionDetails] = useState('');
  const [message, setMessage] = useState('');
  const [activityLogs, setActivityLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [role, setRole] = useState('');
  const [inputAgentId, setInputAgentId] = useState('');
  const [authChecked, setAuthChecked] = useState(false); 

  
  useEffect(() => {
    const storedAgentId = localStorage.getItem('agent_id');
    const storedRole = localStorage.getItem('agent_role');

    if (!storedAgentId || !storedRole) {
      navigate('/agent-login'); // Redirect to login if not authenticated
    } else {
      setAgentId(storedAgentId);
      setRole(storedRole);
    }
    setAuthChecked(true); // Done checking auth
  }, [navigate]);

  const handleSubmitLog = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    const targetAgentId = inputAgentId || agentId;

    if (role === 'ADMIN' && targetAgentId !== agentId) {
      setMessage('Admins can only log their own activities.');
      setLoading(false);
      return;
    }

    try {
      await logAgentActivity(targetAgentId, actionType, actionDetails);
      setMessage('Activity logged successfully!');
      setActionType('');
      setActionDetails('');
      setInputAgentId('');
    } catch (error) {
      handleAuthError(error);
    } finally {
      setLoading(false);
    }
  };

  const handleFetchLogs = async () => {
    setLoading(true);
    setMessage('');
    try {
      let response;
      if (role === 'SUPER_ADMIN') {
        response = await fetchAllLogs(inputAgentId); 
      } else if (role === 'ADMIN') {
        response = await fetchLogsByAgentId(agentId); 
      } else {
        setMessage('Access denied. Unknown role.');
        return;
      }
      setActivityLogs(response);
    } catch (error) {
      handleAuthError(error);
    } finally {
      setLoading(false);
    }
  };

  const handleAuthError = (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      setMessage('Unauthorized. Please log in again.');
      localStorage.clear();
      navigate('/agent-login');
    } else {
      setMessage('Error: ' + (error.response?.data?.message || 'Something went wrong.'));
    }
  };

  if (!authChecked) {
    return <div className="p-8 text-center">Checking authorization...</div>;
  }

  return (
    <div className="p-8 bg-gray-100 min-h-screen">
      <div className="max-w-4xl mx-auto bg-white p-6 shadow-md rounded space-y-8">
        <h2 className="text-2xl font-bold text-blue-700">Agent Activity Log</h2>

        {message && (
          <div
            className={`p-3 rounded ${
              message.includes('success')
                ? 'bg-green-100 text-green-700'
                : 'bg-red-100 text-red-700'
            }`}
          >
            {message}
          </div>
        )}

        <form onSubmit={handleSubmitLog} className="space-y-4">
          {role === 'SUPER_ADMIN' && (
            <input
              type="text"
              value={inputAgentId}
              onChange={(e) => setInputAgentId(e.target.value)}
              placeholder="Target Agent ID (for SUPER_ADMIN)"
              className="w-full p-2 border rounded"
            />
          )}
          <input
            type="text"
            value={actionType}
            onChange={(e) => setActionType(e.target.value)}
            placeholder="Action Type"
            className="w-full p-2 border rounded"
            required
          />
          <input
            type="text"
            value={actionDetails}
            onChange={(e) => setActionDetails(e.target.value)}
            placeholder="Action Details"
            className="w-full p-2 border rounded"
            required
          />
          <button
            type="submit"
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
            disabled={loading}
          >
            {loading ? 'Logging Activity...' : 'Submit Activity'}
          </button>
        </form>

        <div className="space-y-4">
          {role === 'SUPER_ADMIN' && (
            <input
              type="text"
              placeholder="Filter by Agent ID (optional)"
              value={inputAgentId}
              onChange={(e) => setInputAgentId(e.target.value)}
              className="w-full p-2 border rounded"
            />
          )}
          <button
            onClick={handleFetchLogs}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            disabled={loading}
          >
            {loading
              ? 'Fetching Logs...'
              : role === 'SUPER_ADMIN'
              ? 'Fetch All Logs'
              : 'Fetch My Logs'}
          </button>
        </div>

        {activityLogs.length > 0 ? (
          <table className="w-full table-auto text-sm border mt-4">
            <thead>
              <tr className="bg-gray-200">
                <th className="p-2">Log ID</th>
                <th className="p-2">Agent ID</th>
                <th className="p-2">Action Type</th>
                <th className="p-2">Action Details</th>
                <th className="p-2">Timestamp</th>
              </tr>
            </thead>
            <tbody>
              {activityLogs.map((log) => (
                <tr key={log.log_id}>
                  <td className="p-2">{log.log_id}</td>
                  <td className="p-2">{log.agent_id}</td>
                  <td className="p-2">{log.actionType}</td>
                  <td className="p-2">{log.actionDetails}</td>
                  <td className="p-2">{new Date(log.actionTimestamp).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="text-sm text-gray-600 mt-4">No logs found.</p>
        )}
      </div>
    </div>
  );
}

export default AgentActivityLog;
