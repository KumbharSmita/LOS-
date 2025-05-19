import React, { useState } from 'react';
import api from '../services/api';

const LeadsPage = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    panNumber: '',
    aadhaarNumber: '',
    source: '',
    loanType: '',
    amount: '',
    tenureMonths: '',
    purpose: '',
  });

  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');
    try {
      const payload = {
        ...formData,
        amount: parseFloat(formData.amount),
        tenureMonths: parseInt(formData.tenureMonths, 10),
      };
      const response = await api.post('/leads/create', payload);
      setMessage(response.data.message || 'Lead created successfully');
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        panNumber: '',
        aadhaarNumber: '',
        source: '',
        loanType: '',
        amount: '',
        tenureMonths: '',
        purpose: '',
      });
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to create lead');
    }
  };

  return (
    <div className="max-w-3xl mx-auto p-4">
      <h2 className="text-3xl font-bold mb-6">Create New Lead</h2>
      {message && <div className="mb-4 p-2 bg-green-200 text-green-800 rounded">{message}</div>}
      {error && <div className="mb-4 p-2 bg-red-200 text-red-800 rounded">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        {[
          { label: 'First Name', name: 'firstName', type: 'text' },
          { label: 'Last Name', name: 'lastName', type: 'text' },
          { label: 'Email', name: 'email', type: 'email' },
          { label: 'Phone', name: 'phone', type: 'text' },
          { label: 'PAN Number', name: 'panNumber', type: 'text' },
          { label: 'Aadhaar Number', name: 'aadhaarNumber', type: 'text' },
          { label: 'Source', name: 'source', type: 'text' },
          { label: 'Loan Type', name: 'loanType', type: 'text' },
          { label: 'Amount', name: 'amount', type: 'number' },
          { label: 'Tenure Months', name: 'tenureMonths', type: 'number' },
          { label: 'Purpose', name: 'purpose', type: 'text' },
        ].map(({ label, name, type }) => (
          <div key={name}>
            <label htmlFor={name} className="block font-medium mb-1">{label}</label>
            <input
              id={name}
              name={name}
              type={type}
              value={formData[name]}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded px-3 py-2"
              required
            />
          </div>
        ))}
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Create Lead
        </button>
      </form>
    </div>
  );
};

export default LeadsPage;
