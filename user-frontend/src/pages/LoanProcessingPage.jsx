import React, { useState } from 'react';
import { processLoan, getEmiSchedule } from '../api/loans';
import { ClipLoader } from 'react-spinners';

const LoanProcessingPage = () => {
  const [leadsId, setLeadsId] = useState('');
  const [loading, setLoading] = useState(false);
  const [emiSchedule, setEmiSchedule] = useState([]);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleProcessLoan = async () => {
    setLoading(true);
    setError('');
    setMessage('');
    try {
      const result = await processLoan(leadsId);
      setMessage(result); // Show the message from the loan processing result
    } catch (err) {
      setError(
        'Error processing loan: ' + 
        (err?.response?.data || err?.message || 'Unknown error')
      );
    } finally {
      setLoading(false);
    }
  };

  const fetchEmiSchedule = async () => {
    setLoading(true); // Show the loading spinner while fetching EMI schedule
    setError('');
    try {
      const emiData = await getEmiSchedule(leadsId);
      setEmiSchedule(emiData); // Set the EMI schedule data
    } catch (err) {
      setError(
        'Error fetching EMI schedule: ' + 
        (err?.response?.data || err?.message || 'Unknown error')
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-md mx-auto bg-white shadow p-6 rounded">
        <h2 className="text-2xl font-bold text-blue-700 mb-4">Loan Processing & EMI Schedule</h2>

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
          onClick={handleProcessLoan}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition flex justify-center items-center"
          disabled={loading}
        >
          {loading ? (
            <ClipLoader color="#ffffff" loading={true} size={20} />
          ) : (
            'Process Loan'
          )}
        </button>

        {message && <p className="text-green-600 mt-4">{message}</p>}
        {error && <p className="text-red-600 mt-4">{error}</p>}

        {/* New button to fetch EMI schedule */}
        <button
          onClick={fetchEmiSchedule}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition mt-4"
          disabled={loading || !leadsId} // Disable button if no leadsId is entered
        >
          {loading ? (
            <ClipLoader color="#ffffff" loading={true} size={20} />
          ) : (
            'Fetch EMI Schedule'
          )}
        </button>

        {/* Display EMI schedule */}
        {emiSchedule.length > 0 ? (
          <div className="mt-6 p-4 bg-gray-50 rounded shadow">
            <h3 className="font-semibold text-lg mb-2">EMI Schedule</h3>
            <table className="table-auto w-full">
              <thead>
                <tr>
                  <th className="border p-2">EMI ID</th>
                  <th className="border p-2">Due Date</th>
                  <th className="border p-2">EMI Amount</th>
                 
                  <th className="border p-2">Paid Date</th>
                  <th className="border p-2">Status</th>
                  
                    
                </tr>
              </thead>
              <tbody>
                {emiSchedule.map((emi) => (
                  <tr key={emi.emi_id}>
                    <td className="border p-2">{emi.emi_id}</td>
                    <td className="border p-2">{emi.due_date}</td>
                    <td className="border p-2">₹{emi.emi_amount}</td>
                    <td classNAme="border p-2">{emi.paid_date}</td>
                    <td className="border p-2">{emi.status}</td>
                    
                    
                                      </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="mt-4 text-gray-600">No EMI schedule available yet.</p>
        )}
      </div>
    </div>
  );
};

export default LoanProcessingPage;
