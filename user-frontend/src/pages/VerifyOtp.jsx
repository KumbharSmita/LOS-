// src/pages/VerifyOtp.jsx

import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { verifyOtp, resendOtp, getCreditScore } from '../api/auth';

export default function VerifyOtp() {
  const location = useLocation();
  const navigate = useNavigate();

  const { leadsId, email } = location.state || {};

  console.log("VerifyOtp page received state:", location.state);

  const [otp, setOtp] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  if (!leadsId || !email) {
    return (
      <div className="text-red-600 p-4">
        Missing lead data. Please start again from lead form.
      </div>
    );
  }

  const handleVerify = async (e) => {
    e.preventDefault();
    setMessage('');
    setIsSuccess(false);
    setLoading(true);

    try {
      console.log('Verifying OTP:', { leadsId, otp });
      const result = await verifyOtp(leadsId, otp);
      console.log('OTP verification response:', result);

      if (result?.message?.toLowerCase().includes('otp verified')) {
        const scoreResult = await getCreditScore(leadsId);
        console.log('Credit score response:', scoreResult);

        const creditScore = scoreResult.creditScore;
        if (creditScore < 700) {
          navigate('/rejected');
        } else {
          setIsSuccess(true);
          setMessage(result.message);
          navigate('/document-upload', { state: { leadsId } });
        }
      } else {
        setMessage(result?.message || 'OTP verification failed.');
      }
    } catch (err) {
      if (
        err?.response?.status === 400 &&
        err?.response?.data?.message?.toLowerCase()?.includes('low credit score')
      ) {
        navigate('/rejected');
      } else {
        setMessage('Error: ' + (err?.response?.data?.message || err.message || 'Unknown error'));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    setLoading(true);
    setMessage('');
    try {
      const response = await resendOtp(email);
      setMessage(response?.message || 'OTP resent successfully.');
    } catch (err) {
      setMessage('Error: ' + (err?.response?.data?.message || err.message || 'Failed to resend OTP.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white">
      <h2 className="text-xl font-bold text-center mb-4">Verify OTP</h2>

      <form onSubmit={handleVerify}>
        <input
          type="text"
          placeholder="Enter OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          className="w-full p-2 mb-3 border rounded"
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-green-600 text-white p-2 rounded mb-2 hover:bg-green-700"
        >
          {loading ? 'Verifying...' : 'Verify OTP'}
        </button>

        <button
          type="button"
          disabled={loading}
          onClick={handleResend}
          className="w-full bg-gray-600 text-white p-2 rounded hover:bg-gray-700"
        >
          {loading ? 'Resending...' : 'Resend OTP'}
        </button>
      </form>

      {message && (
        <p className={`mt-4 text-center text-sm ${isSuccess ? 'text-green-600' : 'text-red-600'}`}>
          {message}
        </p>
      )}
    </div>
  );
}
