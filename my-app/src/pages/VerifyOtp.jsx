import { useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { verifyOtp, resendOtp } from '../api/auth';

export default function VerifyOtp() {
  const location = useLocation();
  const navigate = useNavigate();
  const { leads_id, email } = location.state || {};

  const [otp, setOtp] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false); // Track successful OTP verification

  // Automatically navigate to /success after 2 seconds if OTP is verified
 useEffect(() => {
    if (otpVerified) {
      const timer = setTimeout(() => {
        navigate('/success'); // Redirect to success page
      }, 2000); // Delay 2 seconds

      return () => clearTimeout(timer); // Clean up the timer when the component is unmounted or otpVerified changes
    }
  }, [otpVerified, navigate]); 

  //  Handle OTP verification
  const handleVerify = async (e) => {
    e.preventDefault();
    setMessage('');
    setLoading(true);

    try {
      const response = await verifyOtp(leads_id, String(otp)); // Ensure OTP is sent as string
      console.log('verifyOtp response:', response);

      const result = response?.data || response;

      if (result?.status?.toLowerCase() === 'success') {
        setMessage('OTP verified successfully.');
        setOtpVerified(true);
      } else {
        setMessage(result?.message || 'OTP verification failed.');
      }
    } catch (err) {
      setMessage('Error: ' + (err?.message || 'Unknown error'));
    } finally {
      setLoading(false);
    }
  };

  
  const handleResend = async () => {
    setLoading(true);
    try {
      const response = await resendOtp(email);
      console.log('resendOtp response:', response);

      const result = response?.data || response;
      setMessage(result?.message || 'OTP resent.');
    } catch (err) {
      setMessage('Error: ' + (err?.message || 'Failed to resend OTP'));
    } finally {
      setLoading(false);
    }
  };

  
  if (!leads_id || !email) {
    return <div className="text-red-600 p-4">Missing lead data.</div>;
  }

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
        <p
          className={`mt-4 text-center text-sm ${
            otpVerified ? 'text-green-600' : 'text-red-600'
          }`}
        >
          {message}
        </p>
      )}
    </div>
  );
}
