import React, { useState, useEffect } from 'react';
import { performUnderwriting, fetchAllUnderwritingResults, fetchUnderwritingByLeadId } from '../api/underwriting'; 

const UnderwritingPage = () => {
  const [leadId, setLeadId] = useState('');
  const [approvedAmount, setApprovedAmount] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [allResults, setAllResults] = useState([]);
  const [singleResult, setSingleResult] = useState(null);
  const [isSingleSearch, setIsSingleSearch] = useState(false); // Toggle to show single or all results

  // Perform underwriting
  const handleUnderwrite = async () => {
    setError('');
    try {
      const data = await performUnderwriting(parseInt(leadId), parseFloat(approvedAmount));
      setResult(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Underwriting failed');
    }
  };

  // Fetch all underwriting results
  const handleFetchAllResults = async () => {
    try {
      const data = await fetchAllUnderwritingResults();
      setAllResults(data);
    } catch (err) {
      setError('Failed to fetch underwriting results');
    }
  };

  // Fetch result by leadId
  const handleFetchByLeadId = async () => {
    try {
      const data = await fetchUnderwritingByLeadId(leadId);
      setSingleResult(data);
    } catch (err) {
      setError('');
    }
  };

  useEffect(() => {
    if (!isSingleSearch) {
      handleFetchAllResults();
    }
  }, [isSingleSearch]); // Fetch all results when the page is loaded

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-xl mx-auto bg-white p-6 rounded shadow space-y-6">
        <h2 className="text-2xl font-bold text-blue-700">Perform Underwriting</h2>

        {/* Underwriting Form */}
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

        <button
          onClick={handleUnderwrite}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          Submit Underwriting
        </button>

        {/* Search Toggle */}
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

        {/* Single Lead Search */}
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
              </div>
            )}
          </div>
        )}

        {/* All Results */}
        {!isSingleSearch && (
          <div className="mt-6">
            {allResults.length > 0 ? (
              <div className="space-y-4">
                {allResults.map((result) => (
                  <div key={result.leadsId} className="bg-gray-100 p-4 rounded shadow">
                    <h3 className="text-lg font-semibold">Lead ID: {result.leadsId}</h3>
                    <p><strong>Decision:</strong> {result.decision}</p>
                    <p><strong>Risk Rating:</strong> {result.riskRating}</p>
                    <p><strong>Approved Amount:</strong> ₹{result.approvedAmount}</p>
                    <p><strong>Notes:</strong> {result.underwriterNotes}</p>
                    <p><strong>Evaluated At:</strong> {new Date(result.evaluatedAt).toLocaleString()}</p>
                  </div>
                ))}
              </div>
            ) : (
              <p>No underwriting results available.</p>
            )}
          </div>
        )}

        {error && <p className="text-red-600 mt-2">{error}</p>}

        {result && (
          <div className="mt-6 bg-gray-100 p-4 rounded">
            <h3 className="text-lg font-semibold mb-2">Underwriting Result:</h3>
            <p><strong>Lead ID:</strong> {result.leadsId}</p>
            <p><strong>Decision:</strong> {result.decision}</p>
            <p><strong>Risk Rating:</strong> {result.riskRating}</p>
            <p><strong>Approved Amount:</strong> ₹{result.approvedAmount}</p>
            <p><strong>Notes:</strong> {result.underwriterNotes}</p>
            <p><strong>Evaluated At:</strong> {new Date(result.evaluatedAt).toLocaleString()}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default UnderwritingPage;
