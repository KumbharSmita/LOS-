import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {
  confirmLoanSelection,
  getLoanConfirmation,
  generateLoanConfirmationOtp,
  resendLoanConfirmationOtp
} from '../api/borrowerSelection';
import { fetchUnderwritingByLeadId } from '../api/underwriting';

function UnderwritingResultPage() {
  const location = useLocation();
  const { leadsId: navLeadId } = location.state || {};
  const leadsId = navLeadId || new URLSearchParams(window.location.search).get('leadId');

  const [underwritingData, setUnderwritingData] = useState(null);
  const [amount, setAmount] = useState('');
  const [tenure, setTenure] = useState('');
  const [otp, setOtp] = useState('');
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [initializing, setInitializing] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      if (!leadsId) return;

      try {
        const data = await fetchUnderwritingByLeadId(leadsId);
        setUnderwritingData(data);
        setAmount(data.approvedAmount);
        if (data.tenureMonths !== undefined && data.tenureMonths !== null) {
          setTenure(data.tenureMonths);
        }

        if (['approved', 'conditional'].includes(data.decision?.toLowerCase())) {
          await generateLoanConfirmationOtp(leadsId);
          setMessage('OTP sent to your email for confirmation.');
        }
      } catch (err) {
        setError('Error fetching underwriting result or generating OTP.');
      }
    };

    fetchData();
  }, [leadsId]);

  useEffect(() => {
    const fetchConfirmation = async () => {
      if (!leadsId) return;

      try {
        const data = await getLoanConfirmation(parseInt(leadsId));
        if (data?.confirmedAmount && data?.confirmedTenureMonths) {
          setAmount(data.confirmedAmount);
          setTenure(data.confirmedTenureMonths);
          setIsConfirmed(true);
        }
      } catch (err) {
        if (err.response?.status !== 204) {
          setError('Error fetching confirmation status.');
        }
      } finally {
        setInitializing(false);
      }
    };

    fetchConfirmation();
  }, [leadsId]);

  const handleConfirmClick = async () => {
    setError('');
    setMessage('');
    setLoading(true);
    try {
      const payload = {
        confirmedAmount: parseFloat(amount),
        confirmedTenureMonths: parseInt(tenure),
      };
      await confirmLoanSelection(parseInt(leadsId), payload, otp);
      setIsConfirmed(true);
      setMessage('Loan selection confirmed successfully.');
    } catch (err) {
      setError(err.response?.data || 'Loan selection confirmation failed.');
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    try {
      await resendLoanConfirmationOtp(leadsId);
      setMessage('OTP resent to your email.');
    } catch (err) {
      setError('Failed to resend OTP.');
    }
  };

  if (!leadsId || !underwritingData) {
    return <div className="p-4 text-center text-red-600">No underwriting result available.</div>;
  }

  if (initializing) {
    return <div className="p-4 text-center">Loading...</div>;
  }

  const {
    decision,
    riskRating,
    approvedAmount,
    rateOfInterest,
    tenureMonths: approvedTenure,
    agentId,
    underwriterNotes,
    evaluatedAt,
  } = underwritingData;

  const decisionLower = decision?.toLowerCase();
  const isApproved = ['approved', 'conditional'].includes(decisionLower);

  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white">
      <h2 className="text-xl font-bold mb-4 text-center text-blue-700">Underwriting Result</h2>

      <div className="space-y-2 text-gray-800">
        <p><strong>Lead ID:</strong> {leadsId}</p>
        <p><strong>Decision:</strong> {decision}</p>
        <p><strong>Risk Rating:</strong> {riskRating}</p>
        <p><strong>Approved Amount:</strong> ₹{approvedAmount}</p>
        <p><strong>Rate of Interest:</strong> {rateOfInterest ? `${rateOfInterest}%` : 'N/A'}</p>
        <p><strong>Tenure (Months):</strong> {approvedTenure ?? 'N/A'}</p>
        <p><strong>Agent ID:</strong> {agentId || 'N/A'}</p>
        <p><strong>Notes:</strong> {underwriterNotes}</p>
        <p><strong>Evaluated At:</strong> {new Date(evaluatedAt).toLocaleString()}</p>
      </div>

      {isApproved ? (
        isConfirmed ? (
          <div className="mt-6 space-y-4">
            <p className="text-center text-green-700 font-semibold">
              Your loan selection has been confirmed successfully.
            </p>
            <div className="bg-gray-50 p-4 rounded shadow text-gray-800">
              <p><strong>Confirmed Amount:</strong> ₹{amount}</p>
              <p><strong>Tenure:</strong> {tenure} months</p>
            </div>
          </div>
        ) : (
          <div className="mt-6 space-y-4">
            <div>
              <label className="block font-medium mb-1">Confirmed Amount (₹)</label>
              <input
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                className="w-full border p-2 rounded"
              />
            </div>
            <div>
              <label className="block font-medium mb-1">Tenure (Months)</label>
              <input
                type="number"
                value={tenure}
                onChange={(e) => setTenure(e.target.value)}
                className="w-full border p-2 rounded"
              />
            </div>
            <div>
              <label className="block font-medium mb-1">Enter OTP</label>
              <input
                type="text"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                className="w-full border p-2 rounded"
                placeholder="Enter OTP sent to email"
              />
              <button onClick={handleResendOtp} className="text-sm text-blue-600 hover:underline mt-1">
                Resend OTP
              </button>
            </div>
            <div className="text-center">
              <button
                onClick={handleConfirmClick}
                disabled={loading}
                className="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
              >
                {loading ? 'Confirming...' : 'Confirm Loan Selection'}
              </button>
            </div>
            {error && <p className="text-red-600 text-center">{error}</p>}
            {message && <p className="text-green-600 text-center">{message}</p>}
          </div>
        )
      ) : (
        <p className="mt-6 text-center text-red-600 font-medium">
          Unfortunately, your loan application was not approved.
        </p>
      )}
    </div>
  );
}

export default UnderwritingResultPage;
