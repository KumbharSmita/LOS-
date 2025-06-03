import React, { useState } from 'react';
import { getRepaymentSchedule } from '../api/repayment';
import { ClipLoader } from 'react-spinners';

const RepaymentSchedulePage = () => {
  const [leadsId, setLeadsId] = useState('');
  const [schedule, setSchedule] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchSchedule = async () => {
    if (!leadsId) {
      setError('Please enter a Lead ID');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const data = await getRepaymentSchedule(leadsId);
      setSchedule(data);
    } catch (err) {
      setError(err?.response?.data || 'Error fetching schedule');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-lg mx-auto bg-white shadow p-6 rounded">
        <h2 className="text-2xl font-bold text-blue-700 mb-4">Repayment Schedule</h2>

        <div className="mb-4">
          <label className="block font-medium mb-1">Lead ID</label>
          <input
            type="number"
            value={leadsId}
            onChange={(e) => setLeadsId(e.target.value)}
            className="w-full border p-2 rounded"
            placeholder="Enter Lead ID"
          />
        </div>

        <button
          onClick={fetchSchedule}
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 flex items-center justify-center"
        >
          {loading ? <ClipLoader size={20} color="#fff" /> : 'Fetch Schedule'}
        </button>

        {error && <p className="text-red-600 mt-4">{error}</p>}

        {schedule && (
          <div className="mt-6 bg-gray-50 p-4 rounded shadow">
            <h3 className="text-lg font-semibold mb-2">Schedule Details</h3>
            <table className="table-auto w-full text-sm">
              <tbody>
                {Object.entries(schedule).map(([key, value]) => (
                  <tr key={key}>
                    <td className="border p-2 font-medium capitalize">{key}</td>
                    <td className="border p-2">{String(value)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default RepaymentSchedulePage;
