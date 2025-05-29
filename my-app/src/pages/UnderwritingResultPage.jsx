import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {
  confirmLoanSelection,
  getLoanConfirmation,
  generateLoanConfirmationOtp,
  resendLoanConfirmationOtp,
} from '../api/borrowerSelection';
import { fetchUnderwritingByLeadId } from '../api/underwriting';
import { submitBankDetails } from '../api/bankDetails';

// Import your disbursement OTP API functions
import {
  generateOtp as generateDisbursementOtp,
  verifyOtp as verifyDisbursementOtp,
  resendOtp as resendDisbursementOtp,
} from '../api/disbursement';

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

  // Bank details states
  const [bankDetails, setBankDetails] = useState({
    accountHolderName: '',
    accountNumber: '',
    ifscCode: '',
  });
  const [bankLoading, setBankLoading] = useState(false);
  const [bankError, setBankError] = useState('');
  const [bankMessage, setBankMessage] = useState('');
  const [bankDetailsSubmitted, setBankDetailsSubmitted] = useState(false);

  // Disbursement OTP states
  const [disbursementOtp, setDisbursementOtp] = useState('');
  const [otpError, setOtpError] = useState('');
  const [otpMessage, setOtpMessage] = useState('');
  const [otpLoading, setOtpLoading] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);

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
          setMessage('OTP sent to your email for loan confirmation.');
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

  // Bank details input handler
  const handleBankInputChange = (e) => {
    const { name, value } = e.target;
    setBankDetails((prev) => ({ ...prev, [name]: value }));
  };

  // Bank details submit handler
  const handleBankDetailsSubmit = async (e) => {
    e.preventDefault();
    setBankError('');
    setBankMessage('');
    setOtpMessage('');
    setOtpError('');
    setOtpVerified(false);
    setDisbursementOtp('');
    setBankLoading(true);
    try {
      await submitBankDetails(leadsId, bankDetails);
      setBankDetailsSubmitted(true);
      setBankMessage('Bank details submitted successfully.');

      // Generate disbursement OTP right after bank details submission
      await generateDisbursementOtp(parseInt(leadsId));
      setOtpMessage('OTP sent to your email for disbursement verification.');
    } catch (err) {
      setBankError(err.response?.data || 'Failed to submit bank details.');
    } finally {
      setBankLoading(false);
    }
  };

  // Disbursement OTP handlers
  const handleDisbursementOtpChange = (e) => {
    setDisbursementOtp(e.target.value);
  };

  const handleVerifyDisbursementOtp = async () => {
    setOtpError('');
    setOtpMessage('');
    setOtpLoading(true);
    try {
      await verifyDisbursementOtp(parseInt(leadsId), disbursementOtp.trim());
      setOtpVerified(true);
      setOtpMessage('Disbursement OTP verified successfully.');
    } catch (err) {
      setOtpError(err.response?.data || 'Invalid OTP, please try again.');
    } finally {
      setOtpLoading(false);
    }
  };

  const handleResendDisbursementOtp = async () => {
    try {
      await resendDisbursementOtp(parseInt(leadsId));
      setOtpMessage('Disbursement OTP resent to your email.');
    } catch (err) {
      setOtpError('Failed to resend disbursement OTP.');
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
        <p>
          <strong>Lead ID:</strong> {leadsId}
        </p>
        <p>
          <strong>Decision:</strong> {decision}
        </p>
        <p>
          <strong>Risk Rating:</strong> {riskRating}
        </p>
        <p>
          <strong>Approved Amount:</strong> ₹{approvedAmount}
        </p>
        <p>
          <strong>Rate of Interest:</strong> {rateOfInterest ? `${rateOfInterest}%` : 'N/A'}
        </p>
        <p>
          <strong>Tenure (Months):</strong> {approvedTenure ?? 'N/A'}
        </p>
        <p>
          <strong>Agent ID:</strong> {agentId || 'N/A'}
        </p>
        <p>
          <strong>Notes:</strong> {underwriterNotes}
        </p>
        <p>
          <strong>Evaluated At:</strong> {new Date(evaluatedAt).toLocaleString()}
        </p>
      </div>

      {isApproved ? (
        isConfirmed ? (
          bankDetailsSubmitted ? (
            otpVerified ? (
              <div className="mt-6 space-y-4">
                <p className="text-center text-green-700 font-semibold">
                  Your loan selection, bank details, and disbursement OTP have been verified successfully.
                </p>
                <div className="bg-gray-50 p-4 rounded shadow text-gray-800">
                  <p>
                    <strong>Confirmed Amount:</strong> ₹{amount}
                  </p>
                  <p>
                    <strong>Tenure:</strong> {tenure} months
                  </p>
                  <p>
                    <strong>Bank Account Holder Name:</strong> {bankDetails.accountHolderName}
                  </p>
                  <p>
                    <strong>Bank Account Number:</strong> {bankDetails.accountNumber}
                  </p>
                  <p>
                    <strong>Bank IFSC Code:</strong> {bankDetails.ifscCode}
                  </p>
                </div>
              </div>
            ) : (
              <div className="mt-6 space-y-4">
                <h3 className="text-lg font-semibold text-center">Verify Disbursement OTP</h3>
                <div>
                  <label className="block font-medium mb-1">Enter OTP</label>
                  <input
                    type="text"
                    value={disbursementOtp}
                    onChange={handleDisbursementOtpChange}
                    className="w-full border p-2 rounded"
                    placeholder="Enter OTP sent to email"
                  />
                  <button
                    onClick={handleResendDisbursementOtp}
                    className="text-sm text-blue-600 hover:underline mt-1"
                    type="button"
                  >
                    Resend OTP
                  </button>
                </div>
                <div className="text-center mt-2">
                  <button
                    onClick={handleVerifyDisbursementOtp}
                    disabled={otpLoading || disbursementOtp.trim().length === 0}
                    className="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
                  >
                    {otpLoading ? 'Verifying...' : 'Verify OTP'}
                  </button>
                </div>
                {otpError && <p className="text-red-600 text-center mt-2">{otpError}</p>}
                {otpMessage && <p className="text-green-600 text-center mt-2">{otpMessage}</p>}
              </div>
            )
          ) : (
            // Bank details form (should not appear after submission)
            <p className="text-center text-red-600">Please submit bank details first.</p>
          )
        ) : (
          // Loan confirmation form
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
              <button onClick={handleResendOtp} className="text-sm text-blue-600 hover:underline mt-1" type="button">
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

      {/* Show bank details form only after loan confirmation and before bank submission */}
      {isApproved && isConfirmed && !bankDetailsSubmitted && (
        <form onSubmit={handleBankDetailsSubmit} className="mt-6 space-y-4">
          <h3 className="text-lg font-semibold text-center">Submit Bank Details for Disbursement</h3>
          <div>
            <label className="block font-medium mb-1">Account Holder Name</label>
            <input
              type="text"
              name="accountHolderName"
              value={bankDetails.accountHolderName}
              onChange={handleBankInputChange}
              className="w-full border p-2 rounded"
              required
            />
          </div>
          <div>
            <label className="block font-medium mb-1">Account Number</label>
            <input
              type="text"
              name="accountNumber"
              value={bankDetails.accountNumber}
              onChange={handleBankInputChange}
              className="w-full border p-2 rounded"
              required
            />
          </div>
          <div>
            <label className="block font-medium mb-1">IFSC Code</label>
            <input
              type="text"
              name="ifscCode"
              value={bankDetails.ifscCode}
              onChange={handleBankInputChange}
              className="w-full border p-2 rounded"
              required
            />
          </div>
          <div className="text-center">
            <button
              type="submit"
              disabled={bankLoading}
              className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
            >
              {bankLoading ? 'Submitting...' : 'Submit Bank Details'}
            </button>
          </div>
          {bankError && <p className="text-red-600 text-center">{bankError}</p>}
          {bankMessage && <p className="text-green-600 text-center">{bankMessage}</p>}
        </form>
      )}
    </div>
  );
}

export default UnderwritingResultPage;
