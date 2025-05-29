import React, { useState, useEffect } from 'react';
import {
  performUnderwriting,
  fetchAllUnderwritingResults,
  fetchUnderwritingByLeadId
} from '../api/underwriting';

const UnderwritingPage = () => {
  const [leadId, setLeadId] = useState('');
  const [approvedAmount, setApprovedAmount] = useState('');
  const [rateOfInterest, setRateOfInterest] = useState('');
  const [tenureMonths, setTenureMonths] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [allResults, setAllResults] = useState([]);
  const [singleResult, setSingleResult] = useState(null);
  const [isSingleSearch, setIsSingleSearch] = useState(false);

 const handleUnderwrite = async () => {
  setError('');
  try {
    // Log the raw input before parsing
    console.log('Submitting underwriting with tenureMonths (raw):', tenureMonths);

    // Validate tenureMonths is a positive integer
    const parsedTenure = parseInt(tenureMonths, 10);

    // Log parsed value
    console.log('Parsed Tenure:', parsedTenure);

    if (isNaN(parsedTenure) || parsedTenure <= 0) {
      setError('Tenure must be a valid positive number');
      return;
    }

    const data = await performUnderwriting(
      parseInt(leadId, 10),
      parseFloat(approvedAmount),
      parseFloat(rateOfInterest),
      parsedTenure
    );

    setResult(data);
    setLeadId('');
    setApprovedAmount('');
    setRateOfInterest('');
    setTenureMonths('');
  } catch (err) {
    setError(err.response?.data?.message || 'Underwriting failed');
  }
};


  const handleFetchAllResults = async () => {
    setError('');
    try {
      const data = await fetchAllUnderwritingResults();
      setAllResults(data);
    } catch (err) {
      setError('Failed to fetch underwriting results');
    }
  };

  const handleFetchByLeadId = async () => {
    setError('');
    try {
      const data = await fetchUnderwritingByLeadId(leadId);
      setSingleResult(data);
    } catch (err) {
      setError('No underwriting result found for this Lead ID');
    }
  };

  useEffect(() => {
    if (!isSingleSearch) {
      handleFetchAllResults();
    }
  }, [isSingleSearch]);

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-xl mx-auto bg-white p-6 rounded shadow space-y-6">
        <h2 className="text-2xl font-bold text-blue-700">Perform Underwriting</h2>

        <div>
          <label className="block mb-1 font-medium">Lead ID</label>
          <input
            type="number"
            value={leadId}
            onChange={(e) => setLeadId(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Approved Amount</label>
          <input
            type="number"
            value={approvedAmount}
            onChange={(e) => setApprovedAmount(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Rate of Interest (%)</label>
          <input
            type="number"
            value={rateOfInterest}
            onChange={(e) => setRateOfInterest(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <div>
          <label className="block mb-1 font-medium">Tenure (Months)</label>
          <input
            type="number"
            min={1}
            value={tenureMonths}
            onChange={(e) => setTenureMonths(e.target.value)}
            className="w-full border p-2 rounded"
          />
        </div>

        <button
          onClick={handleUnderwrite}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          Submit Underwriting
        </button>

        <div className="flex justify-between mt-6">
          <button
            onClick={() => setIsSingleSearch(true)}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
          >
            View Single Lead Result
          </button>

          <button
            onClick={() => setIsSingleSearch(false)}
            className="bg-orange-600 text-white px-4 py-2 rounded hover:bg-orange-700 transition"
          >
            View All Results
          </button>
        </div>

        {isSingleSearch && (
          <div className="mt-4">
            <button
              onClick={handleFetchByLeadId}
              className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 transition"
            >
              Get Underwriting Result by Lead ID
            </button>

            {singleResult && (
              <div className="mt-6 bg-gray-100 p-4 rounded">
                <h3 className="text-lg font-semibold mb-2">Underwriting Result:</h3>
                <p><strong>Lead ID:</strong> {singleResult.leadsId}</p>
                <p><strong>Decision:</strong> {singleResult.decision}</p>
                <p><strong>Risk Rating:</strong> {singleResult.riskRating}</p>
                <p><strong>Approved Amount:</strong> ₹{singleResult.approvedAmount}</p>
                <p><strong>Notes:</strong> {singleResult.underwriterNotes}</p>
                <p><strong>Evaluated At:</strong> {new Date(singleResult.evaluatedAt).toLocaleString()}</p>
                <p><strong>Agent ID:</strong> {singleResult.agentId}</p>
                <p><strong>Tenure (Months):</strong> {singleResult.tenureMonths}</p>
                <p><strong>Rate of Interest:</strong> {singleResult.rateOfInterest}%</p>
              </div>
            )}
          </div>
        )}

        {!isSingleSearch && (
          <div className="mt-6">
            {allResults.length > 0 ? (
              allResults.map((res) => (
                <div key={res.leadsId} className="bg-gray-100 p-4 rounded shadow mb-4">
                  <h3 className="text-lg font-semibold">Lead ID: {res.leadsId}</h3>
                  <p><strong>Decision:</strong> {res.decision}</p>
                  <p><strong>Risk Rating:</strong> {res.riskRating}</p>
                  <p><strong>Approved Amount:</strong> ₹{res.approvedAmount}</p>
                  <p><strong>Notes:</strong> {res.underwriterNotes}</p>
                  <p><strong>Evaluated At:</strong> {new Date(res.evaluatedAt).toLocaleString()}</p>
                  <p><strong>Agent ID:</strong> {res.agentId}</p>
                  <p><strong>Tenure (Months):</strong> {res.tenureMonths}</p>
                  <p><strong>Rate of Interest:</strong> {res.rateOfInterest}%</p>
                </div>
              ))
            ) : (
              <p>No underwriting results available.</p>
            )}
          </div>
        )}

        {error && <p className="text-red-600 mt-4">{error}</p>}

        {result && (
          <div className="mt-6 bg-gray-100 p-4 rounded">
            <h3 className="text-lg font-semibold mb-2">Underwriting Completed:</h3>
            <p><strong>Lead ID:</strong> {result.leadsId}</p>
            <p><strong>Decision:</strong> {result.decision}</p>
            <p><strong>Risk Rating:</strong> {result.riskRating}</p>
            <p><strong>Approved Amount:</strong> ₹{result.approvedAmount}</p>
            <p><strong>Notes:</strong> {result.underwriterNotes}</p>
            <p><strong>Evaluated At:</strong> {new Date(result.evaluatedAt).toLocaleString()}</p>
            <p><strong>Agent ID:</strong> {result.agentId}</p>
            <p><strong>Tenure (Months):</strong> {result.tenureMonths}</p>
            <p><strong>Rate of Interest:</strong> {result.rateOfInterest}%</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default UnderwritingPage;
