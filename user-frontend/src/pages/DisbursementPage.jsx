import React, { useState } from 'react';
import {
  disburseLoan,
  getDisbursementByLeadId,
} from '../api/disbursement';

const DisbursementPage = () => {
  const [leadsId, setLeadsId] = useState('');
  const [disbursementResult, setDisbursementResult] = useState(null);
  const [searchResult, setSearchResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Validate leadsId is positive integer
  const isValidLeadId = () => {
    const id = Number(leadsId);
    return Number.isInteger(id) && id > 0;
  };

  const handleDisburse = async () => {
    if (!isValidLeadId()) {
      alert('Please enter a valid Lead ID');
      return;
    }
    setError('');
    setLoading(true);
    try {
      const payload = { leadsId: parseInt(leadsId) };
      const result = await disburseLoan(payload);
      setDisbursementResult(result);
      setSearchResult(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Disbursement failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!isValidLeadId()) {
      alert('Please enter a valid Lead ID');
      return;
    }
    setError('');
    setLoading(true);
    try {
      const result = await getDisbursementByLeadId(parseInt(leadsId));
      setSearchResult(result);
      setDisbursementResult(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Search failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-xl mx-auto bg-white p-6 rounded shadow space-y-6">
        <h2 className="text-2xl font-bold text-blue-700">Loan Disbursement</h2>

        <div>
          <label className="block font-medium">Lead ID</label>
          <input
            type="number"
            value={leadsId}
            onChange={(e) => setLeadsId(e.target.value)}
            className="w-full border p-2 rounded"
            min={1}
          />
        </div>

        <div className="flex gap-4">
          <button
            onClick={handleDisburse}
            disabled={loading || !isValidLeadId()}
            className={`text-white px-4 py-2 rounded transition ${
              !loading && isValidLeadId()
                ? 'bg-green-600 hover:bg-green-700'
                : 'bg-gray-400 cursor-not-allowed'
            }`}
          >
            {loading ? 'Processing...' : 'Disburse Loan'}
          </button>
          <button
            onClick={handleSearch}
            disabled={loading || !isValidLeadId()}
            className={`text-white px-4 py-2 rounded transition ${
              !loading && isValidLeadId()
                ? 'bg-blue-600 hover:bg-blue-700'
                : 'bg-gray-400 cursor-not-allowed'
            }`}
          >
            {loading ? 'Loading...' : 'Get Disbursement by Lead ID'}
          </button>
        </div>

        {error && <p className="text-red-600">{error}</p>}

        {disbursementResult && (
          <div className="bg-gray-50 p-4 mt-4 rounded shadow">
            <h3 className="font-semibold text-lg">Disbursement Success</h3>
            <p><strong>Leads Id:</strong> {disbursementResult.leadsId}</p>
            <p><strong>Approved Amount:</strong> ₹{disbursementResult.approvedAmount}</p>
            <p><strong>Processing Fee:</strong> ₹{disbursementResult.processingFee}</p>
            <p><strong>Disbursed Amount:</strong> ₹{disbursementResult.disbursedAmount}</p>
            <p><strong>UTR Number:</strong> {disbursementResult.utrNumber}</p>
            <p><strong>Status:</strong> {disbursementResult.status}</p>
            <p><strong>Disbursed At:</strong> {new Date(disbursementResult.disbursedAt).toLocaleString()}</p>
          </div>
        )}

        {searchResult && (
          <div className="bg-gray-50 p-4 mt-4 rounded shadow">
            <h3 className="font-semibold text-lg">Disbursement Info</h3>
            <p><strong>Lead ID:</strong> {searchResult.leadsId}</p>
            <p><strong>Approved Amount:</strong> ₹{searchResult.approvedAmount}</p>
            <p><strong>Disbursed Amount:</strong> ₹{searchResult.disbursedAmount}</p>
            <p><strong>Bank Account:</strong> {searchResult.bankAccount || '-'}</p>
            <p><strong>UTR Number:</strong> {searchResult.utrNumber}</p>
            <p><strong>Status:</strong> {searchResult.status}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default DisbursementPage;
