import React, { useState } from 'react';
import { disburseLoan, getDisbursementByLeadId } from '../api/disbursement';

const DisbursementPage = () => {
  const [leadsId, setLeadsId] = useState('');
  const [bankAccount, setBankAccount] = useState('');
  const [rateOfInterest, setRateOfInterest] = useState('');
  const [disbursementResult, setDisbursementResult] = useState(null);
  const [searchResult, setSearchResult] = useState(null);
  const [error, setError] = useState('');

  const handleDisburse = async () => {
    try {
      setError('');
      const payload = {
        leadsId: parseInt(leadsId),
        bankAccount,
        rateOfInterest: parseFloat(rateOfInterest)
      };
      const result = await disburseLoan(payload);
      setDisbursementResult(result);
    } catch (err) {
      setError(err.response?.data?.message || 'Disbursement failed');
    }
  };

  const handleSearch = async () => {
    try {
      setError('');
      const result = await getDisbursementByLeadId(parseInt(leadsId));
      setSearchResult(result);
    } catch (err) {
      setError(err.response?.data?.message || 'Search failed');
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
          />
        </div>

        <div>
          <label className="block font-medium">Bank Account Number</label>
          <input
            type="text"
            value={bankAccount}
            onChange={(e) => setBankAccount(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div>
          <label className="block font-medium">Rate of Interest (%)</label>
          <input
            type="number"
            value={rateOfInterest}
            onChange={(e) => setRateOfInterest(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div className="flex gap-4">
          <button
            onClick={handleDisburse}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
          >
            Disburse Loan
          </button>
          <button
            onClick={handleSearch}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
          >
            Get Disbursement by Lead ID
          </button>
        </div>

        {error && <p className="text-red-600">{error}</p>}

        {disbursementResult && (
          <div className="bg-gray-50 p-4 mt-4 rounded shadow">
            <h3 className="font-semibold text-lg">Disbursement Success</h3>
            <p><strong>Leads Id:</strong>{disbursementResult.leadsId}</p>
            <p><strong>Approved Amount:</strong>{disbursementResult.approvedAmount}</p>
            <p><strong>Rate of Interest:</strong>{disbursementResult.rateOfInterest}</p>
            <p><strong>Processing Fee:</strong>{disbursementResult.processingFee}</p>

            <p><strong>UTR Number:</strong> {disbursementResult.utrNumber}</p>
            
            <p><strong>Status:</strong> {disbursementResult.status}</p>
           
            <p><strong>Disbursed Amount:</strong> ₹{disbursementResult.disbursedAmount}</p>
             <p><strong>Disbursed At:</strong> {new Date(disbursementResult.disbursedAt).toLocaleString()}</p>
          </div>
        )}

        {searchResult && (
          <div className="bg-gray-50 p-4 mt-4 rounded shadow">
            <h3 className="font-semibold text-lg">Disbursement Info</h3>
            <p><strong>Lead ID:</strong> {searchResult.leadsId}</p>
            <p><strong>Approved Amount:</strong> ₹{searchResult.approvedAmount}</p>
            <p><strong>Disbursed Amount:</strong> ₹{searchResult.disbursedAmount}</p>
            <p><strong>Bank Account:</strong> {searchResult.bankAccount}</p>
            <p><strong>UTR Number:</strong> {searchResult.utrNumber}</p>
            <p><strong>Status:</strong> {searchResult.status}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default DisbursementPage;
