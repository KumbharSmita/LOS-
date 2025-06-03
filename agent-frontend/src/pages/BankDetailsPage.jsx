import React, { useState } from 'react';
import { submitBankDetails } from '../api/bankDetails';

const BankDetailsPage = () => {
  const [leadId, setLeadId] = useState('');
  const [accountHolderName, setAccountHolderName] = useState('');
  const [accountNumber, setAccountNumber] = useState('');
  const [ifscCode, setIfscCode] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');

    if (!leadId || !accountHolderName || !accountNumber || !ifscCode) {
      setError('Please fill all fields');
      return;
    }

    try {
      const data = {
        leadsId: parseInt(leadId, 10),
        accountHolderName,
        accountNumber,
        ifscCode,
      };
      await submitBankDetails(leadId, data);
      setMessage('Bank details submitted successfully!');
      // Reset form
      setLeadId('');
      setAccountHolderName('');
      setAccountNumber('');
      setIfscCode('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit bank details');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-6">Submit Bank Details for Disbursement</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block font-medium">Lead ID</label>
          <input
            type="number"
            value={leadId}
            onChange={(e) => setLeadId(e.target.value)}
            className="w-full border p-2 rounded"
            required
          />
        </div>

        <div>
          <label className="block font-medium">Account Holder Name</label>
          <input
            type="text"
            value={accountHolderName}
            onChange={(e) => setAccountHolderName(e.target.value)}
            className="w-full border p-2 rounded"
            required
          />
        </div>

        <div>
          <label className="block font-medium">Account Number</label>
          <input
            type="text"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
            className="w-full border p-2 rounded"
            required
          />
        </div>

        <div>
          <label className="block font-medium">IFSC Code</label>
          <input
            type="text"
            value={ifscCode}
            onChange={(e) => setIfscCode(e.target.value)}
            className="w-full border p-2 rounded"
            maxLength={11}
            required
          />
        </div>

        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          Submit Bank Details
        </button>
      </form>

      {message && <p className="mt-4 text-green-600">{message}</p>}
      {error && <p className="mt-4 text-red-600">{error}</p>}
    </div>
  );
};

export default BankDetailsPage;
