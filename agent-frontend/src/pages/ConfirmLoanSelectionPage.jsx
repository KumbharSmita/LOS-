import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {
  confirmLoanSelection,
  getLoanConfirmation,
  generateLoanConfirmationOtp,
  resendLoanConfirmationOtp
} from '../api/borrowerSelection';

const ConfirmLoanSelectionPage = () => {
  const location = useLocation();
  const { leadId: passedLeadId, approvedAmount: passedApprovedAmount } = location.state || {};

  const [leadId, setLeadId] = useState(passedLeadId || '');
  const [amount, setAmount] = useState(passedApprovedAmount || '');
  const [tenure, setTenure] = useState('');
  const [otp, setOtp] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (leadId) {
      getLoanConfirmation(parseInt(leadId))
        .then(data => {
          if (data?.confirmedAmount && data?.confirmedTenureMonths) {
            setResult(data);
            setIsConfirmed(true);
            setAmount(data.confirmedAmount);
            setTenure(data.confirmedTenureMonths);
          } else {
            // Only generate OTP if not confirmed yet
            generateLoanConfirmationOtp(parseInt(leadId))
              .then(() => setMessage("OTP sent to your email."))
              .catch(() => setError("Failed to send OTP."));
          }
        })
        .catch(err => {
          if (!(err.response && err.response.status === 204)) {
            setError("Failed to fetch loan confirmation status.");
          }
        })
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [leadId]);

  const handleConfirm = async () => {
    setError('');
    try {
      const payload = {
        confirmedAmount: parseFloat(amount),
        confirmedTenureMonths: parseInt(tenure),
      };
      const data = await confirmLoanSelection(parseInt(leadId), payload, otp);
      setResult(data);
      setIsConfirmed(true);
      setMessage("Loan selection confirmed successfully.");
    } catch (err) {
      setError(err.response?.data || 'Loan selection confirmation failed.');
    }
  };

  const handleResendOtp = async () => {
    setError('');
    try {
      await resendLoanConfirmationOtp(parseInt(leadId));
      setMessage('OTP resent to your email.');
    } catch (err) {
      setError('Failed to resend OTP.');
    }
  };

  if (loading) return <p>Loading...</p>;

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
            disabled={!!passedLeadId || isConfirmed}
          />
        </div>

        <div className="mb-4">
          <label className="block font-medium mb-1">Confirmed Amount (₹)</label>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            className="w-full border p-2 rounded"
            disabled={isConfirmed}
          />
        </div>

        <div className="mb-4">
          <label className="block font-medium mb-1">Tenure (Months)</label>
          <input
            type="number"
            value={tenure}
            onChange={(e) => setTenure(e.target.value)}
            className="w-full border p-2 rounded"
            disabled={isConfirmed}
          />
        </div>

        {!isConfirmed && (
          <>
            <div className="mb-4">
              <label className="block font-medium mb-1">Enter OTP</label>
              <input
                type="text"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                className="w-full border p-2 rounded"
                placeholder="Enter OTP sent to your email"
              />
              <button
                type="button"
                onClick={handleResendOtp}
                className="text-sm text-blue-600 hover:underline mt-1"
              >
                Resend OTP
              </button>
            </div>
          </>
        )}

        <button
          onClick={handleConfirm}
          disabled={isConfirmed}
          className={`px-4 py-2 rounded transition ${
            isConfirmed
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-green-600 text-white hover:bg-green-700'
          }`}
        >
          {isConfirmed ? 'Already Confirmed' : 'Confirm Selection'}
        </button>

        {message && <p className="text-green-600 mt-4">{message}</p>}
        {error && <p className="text-red-600 mt-4">{error}</p>}

        {result && (
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
