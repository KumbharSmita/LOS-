import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  performUnderwriting,
  fetchAllUnderwritingResults,
  fetchMyUnderwritingResults,
  fetchUnderwritingByLeadId,
} from '../api/underwriting';

function UnderwritingResults() {
  const navigate = useNavigate();

  const [role, setRole] = useState('');
  const [agentId, setAgentId] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [authChecked, setAuthChecked] = useState(false);

  // Form states
  const [leadId, setLeadId] = useState('');
  const [approvedAmount, setApprovedAmount] = useState('');
  const [rateOfInterest, setRateOfInterest] = useState('');
  const [tenureMonths, setTenureMonths] = useState('');

  // Search by Lead ID (Super Admin)
  const [searchLeadId, setSearchLeadId] = useState('');
  const [singleResult, setSingleResult] = useState(null);
  const [singleResultMessage, setSingleResultMessage] = useState('');

  useEffect(() => {
    const storedAgentId = localStorage.getItem('agent_id');
    const storedRole = localStorage.getItem('agent_role');

    if (!storedAgentId || !storedRole) {
      navigate('/agent-login');
    } else {
      setAgentId(storedAgentId);
      setRole(storedRole);
      setAuthChecked(true);
    }
  }, [navigate]);

  // Utility: Normalize backend data keys to frontend keys
  const normalizeResult = (res) => {
    return {
      id: res.resultId ?? res.id,
      leadId: res.leadsId ?? res.leadId,
      approvedAmount: res.approvedAmount,
      rateOfInterest: res.rateOfInterest,
      tenureMonths: res.tenureMonths,
      riskRating: res.riskRating,
      decision: res.decision,
      processedDate: res.evaluatedAt ?? res.processedDate,
      agentId: res.agentId,
    };
  };

  const handleFetchResults = async () => {
    setLoading(true);
    setMessage('');
    setSingleResult(null);
    setSingleResultMessage('');
    try {
      const response =
        role === 'SUPER_ADMIN'
          ? await fetchAllUnderwritingResults()
          : await fetchMyUnderwritingResults(agentId);

      // Normalize all results before setting state
      const normalizedResults = Array.isArray(response)
        ? response.map(normalizeResult)
        : [];

      setResults(normalizedResults);

      if (!response || response.length === 0) {
        setMessage('No underwriting results found.');
      }
    } catch (error) {
      handleAuthError(error);
    } finally {
      setLoading(false);
    }
  };

  const handleUnderwrite = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    try {
      await performUnderwriting({
        leadId,
        approvedAmount: parseFloat(approvedAmount),
        rateOfInterest: parseFloat(rateOfInterest),
        tenureMonths: parseInt(tenureMonths, 10),
        agentId,
      });
      setMessage(' Underwriting submitted successfully!');
      setLeadId('');
      setApprovedAmount('');
      setRateOfInterest('');
      setTenureMonths('');
      await handleFetchResults();
    } catch (error) {
      handleAuthError(error);
    } finally {
      setLoading(false);
    }
  };

  const handleFetchByLeadId = async () => {
    setSingleResultMessage('');
    setSingleResult(null);
    if (!searchLeadId.trim()) {
      setSingleResultMessage('Please enter a Lead ID to search.');
      return;
    }
    setLoading(true);
    try {
      const result = await fetchUnderwritingByLeadId(searchLeadId.trim());

      if (!result) {
        setSingleResultMessage('No underwriting result found for this Lead ID.');
      } else {
        setSingleResult(normalizeResult(result));
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        setSingleResultMessage('No underwriting result found for this Lead ID.');
      } else {
        setSingleResultMessage('Error fetching underwriting result.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleAuthError = (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      setMessage(' Unauthorized. Please log in again.');
      localStorage.clear();
      navigate('/agent-login');
    } else {
      setMessage(' Error: ' + (error.response?.data?.message || 'Something went wrong.'));
    }
  };

  if (!authChecked) {
    return (
      <div className="p-8 text-center text-gray-600 font-medium text-lg">
        Checking authorization...
      </div>
    );
  }

  const formatCurrency = (value) => {
    if (value === undefined || value === null) return 'N/A';
    return Number(value).toLocaleString('en-IN', {
      style: 'currency',
      currency: 'INR',
      minimumFractionDigits: 2,
    });
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const d = new Date(dateString);
    return isNaN(d.getTime()) ? 'N/A' : d.toLocaleString();
  };

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto bg-white p-8 rounded-lg shadow-lg space-y-8">
        <h2 className="text-3xl font-extrabold text-blue-800">Underwriting Results</h2>

        {message && (
          <div
            className={`p-4 rounded ${
              message.includes('success') || message.includes('')
                ? 'bg-green-100 text-green-800'
                : message.includes('Unauthorized') || message.includes('')
                ? 'bg-yellow-100 text-yellow-800'
                : 'bg-red-100 text-red-800'
            }`}
          >
            {message}
          </div>
        )}

        {/* Underwriting Submission Form */}
        <form onSubmit={handleUnderwrite} className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <input
            type="text"
            value={leadId}
            onChange={(e) => setLeadId(e.target.value)}
            placeholder="Lead ID"
            className="p-3 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
          <input
            type="number"
            value={approvedAmount}
            onChange={(e) => setApprovedAmount(e.target.value)}
            placeholder="Approved Amount"
            className="p-3 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
            min={0}
            step="0.01"
          />
          <input
            type="number"
            value={rateOfInterest}
            onChange={(e) => setRateOfInterest(e.target.value)}
            placeholder="Rate of Interest (%)"
            className="p-3 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
            min={0}
            step="0.01"
          />
          <input
            type="number"
            value={tenureMonths}
            onChange={(e) => setTenureMonths(e.target.value)}
            placeholder="Tenure (months)"
            className="p-3 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
            min={1}
          />
          <div className="md:col-span-4 flex justify-end mt-2">
            <button
              type="submit"
              className="bg-green-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-green-700 disabled:bg-green-400 transition"
              disabled={loading}
            >
              {loading ? 'Submitting...' : 'Submit Underwriting'}
            </button>
          </div>
        </form>

        {/* Fetch Results Button and Search for Super Admin */}
        <div className="flex flex-wrap justify-center space-x-4 gap-4">
          <button
            onClick={handleFetchResults}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-700 disabled:bg-blue-400 transition"
            disabled={loading}
          >
            {loading ? 'Fetching Results...' : 'Fetch Underwriting Results'}
          </button>

          {role === 'SUPER_ADMIN' && (
            <div className="flex items-center space-x-2">
              <input
                type="text"
                value={searchLeadId}
                onChange={(e) => setSearchLeadId(e.target.value)}
                placeholder="Search by Lead ID"
                className="p-2 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
              />
              <button
                onClick={handleFetchByLeadId}
                className="bg-indigo-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-indigo-700 disabled:bg-indigo-400 transition"
                disabled={loading}
              >
                {loading ? 'Searching...' : 'Search'}
              </button>
            </div>
          )}
        </div>

        {/* Single Search Result */}
        {singleResultMessage && (
          <div
            className={`p-4 rounded ${
              singleResultMessage.includes('No underwriting')
                ? 'bg-yellow-100 text-yellow-800'
                : 'bg-red-100 text-red-800'
            }`}
          >
            {singleResultMessage}
          </div>
        )}
        {singleResult && (
          <div className="overflow-x-auto border rounded shadow p-4 mt-4">
            <table className="min-w-full border-collapse border border-gray-300 text-sm text-left">
              <thead className="bg-gray-100">
                <tr>
                  <th className="p-2 border border-gray-300">Lead ID</th>
                  <th className="p-2 border border-gray-300">Approved Amount</th>
                  <th className="p-2 border border-gray-300">Rate of Interest (%)</th>
                  <th className="p-2 border border-gray-300">Tenure (Months)</th>
                  <th className="p-2 border border-gray-300">Risk Rating</th>
                  <th className="p-2 border border-gray-300">Decision</th>
                  <th className="p-2 border border-gray-300">Processed Date</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td className="p-2 border border-gray-300">{singleResult.leadId}</td>
                  <td className="p-2 border border-gray-300">{formatCurrency(singleResult.approvedAmount)}</td>
                  <td className="p-2 border border-gray-300">{singleResult.rateOfInterest}</td>
                  <td className="p-2 border border-gray-300">{singleResult.tenureMonths}</td>
                  <td className="p-2 border border-gray-300">{singleResult.riskRating}</td>
                  <td className="p-2 border border-gray-300">{singleResult.decision}</td>
                  <td className="p-2 border border-gray-300">{formatDate(singleResult.processedDate)}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}

        {/* All Results Table */}
        {results.length > 0 && (
          <div className="overflow-x-auto border rounded shadow p-4">
            <table className="min-w-full border-collapse border border-gray-300 text-sm text-left">
              <thead className="bg-gray-100">
                <tr>
                  <th className="p-2 border border-gray-300">Lead ID</th>
                  <th className="p-2 border border-gray-300">Approved Amount</th>
                  <th className="p-2 border border-gray-300">Rate of Interest (%)</th>
                  <th className="p-2 border border-gray-300">Tenure (Months)</th>
                  <th className="p-2 border border-gray-300">Risk Rating</th>
                  <th className="p-2 border border-gray-300">Decision</th>
                  <th className="p-2 border border-gray-300">Processed Date</th>
                  {role === 'SUPER_ADMIN' && <th className="p-2 border border-gray-300">Agent ID</th>}
                </tr>
              </thead>
              <tbody>
                {results.map((res) => (
                  <tr key={res.id}>
                    <td className="p-2 border border-gray-300">{res.leadId}</td>
                    <td className="p-2 border border-gray-300">{formatCurrency(res.approvedAmount)}</td>
                    <td className="p-2 border border-gray-300">{res.rateOfInterest}</td>
                    <td className="p-2 border border-gray-300">{res.tenureMonths}</td>
                    <td className="p-2 border border-gray-300">{res.riskRating}</td>
                    <td className="p-2 border border-gray-300">{res.decision}</td>
                    <td className="p-2 border border-gray-300">{formatDate(res.processedDate)}</td>
                    {role === 'SUPER_ADMIN' && <td className="p-2 border border-gray-300">{res.agentId}</td>}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default UnderwritingResults;
