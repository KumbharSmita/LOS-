import React, { useState } from 'react';
import { confirmLoanSelection } from '../api/borrowerSelection';

const ConfirmLoanSelectionPage = () => {
  const [leadId, setLeadId] = useState('');
  const [amount, setAmount] = useState('');
  const [tenure, setTenure] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  const handleConfirm = async () => {
    setError('');
    try {
      const payload = {
        confirmedAmount: parseFloat(amount),
        confirmedTenureMonths: parseInt(tenure),
      };
      const data = await confirmLoanSelection(parseInt(leadId), payload);
      setResult(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Loan selection confirmation failed.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-md mx-auto bg-white shadow p-6 rounded">
        <h2 className="text-2xl font-bold text-blue-700 mb-4">Confirm Loan Selection</h2>

        <div className="mb-4">
          <label className="block font-medium mb-1">Lead ID</label>
          <input
            type="number"
            value={leadId}
            onChange={(e) => setLeadId(e.target.value)}
            className="w-full border p-2 rounded"
            placeholder="Enter Lead ID"
          />
        </div>

        <div className="mb-4">
          <label className="block font-medium mb-1">Confirmed Amount (₹)</label>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            className="w-full border p-2 rounded"
            placeholder="Enter Amount"
          />
        </div>

        <div className="mb-4">
          <label className="block font-medium mb-1">Tenure (Months)</label>
          <input
            type="number"
            value={tenure}
            onChange={(e) => setTenure(e.target.value)}
            className="w-full border p-2 rounded"
            placeholder="Enter Tenure"
          />
        </div>

        <button
          onClick={handleConfirm}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
        >
          Confirm Selection
        </button>

        {error && <p className="text-red-600 mt-4">{error}</p>}

        {result?.confirmedAmount && (
          <div className="mt-6 p-4 bg-gray-50 rounded shadow">
            <h3 className="font-semibold text-lg mb-2">Confirmation Successful</h3>
            <p><strong>Confirmed Amount:</strong> ₹{result.confirmedAmount}</p>
            <p><strong>Tenure:</strong> {result.confirmedTenureMonths} months</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ConfirmLoanSelectionPage;
