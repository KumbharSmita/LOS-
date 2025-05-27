import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { loginUser } from '../api/userApi';
import { getLeadStatus } from '../api/underwritingApi';
import { getDisbursementByLeadId } from '../api/disbursement';
import { getEmiSchedule } from '../api/loans';

const UserLogin = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setErrorMessage('');

    try {
      // Login API call
      const response = await loginUser(formData);
      alert(response.data.message);
      localStorage.setItem('userEmail', formData.email);

      // Step 1: Get Lead Status
      let data;
      try {
        const statusResponse = await getLeadStatus(formData.email);
        data = statusResponse.data;
      } catch {
        data = null;
      }

      // If no lead, redirect to lead form
      if (!data || !data.leadsId) {
        navigate('/lead-form');
        return;
      }

      // Step 2: Check disbursement status
      let disbursementData = null;
      try {
        disbursementData = await getDisbursementByLeadId(data.leadsId);
      } catch {
        disbursementData = null;
      }

      if (disbursementData && disbursementData.status === 'SUCCESS') {
        // Step 3: Fetch EMI schedule if disbursed
        let emiSchedule = [];
        try {
          emiSchedule = await getEmiSchedule(data.leadsId);
        } catch {
          emiSchedule = [];
        }

        navigate('/disbursement-status', {
          state: { disbursement: disbursementData, emiSchedule },
        });
        return;
      }

      // Step 4: If underwriting decision available
      if (data.decision) {
        navigate('/underwriting-result', {
          state: {
            leadsId: data.leadsId,
            decision: data.decision,
            riskRating: data.riskRating,
            approvedAmount: data.approvedAmount,
            underwriterNotes: data.underwriterNotes,
            evaluatedAt: data.evaluatedAt,
          },
        });
        return;
      }

      // Step 5: Otherwise, go to lead status (likely pending)
      navigate('/lead-status', {
        state: {
          leadsId: data.leadsId,
          status: data.status || 'Pending',
        },
      });
    } catch (err) {
      console.error(err);
      setErrorMessage('Login failed. Please check your credentials and try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center text-blue-700">User Login</h2>

      <form onSubmit={handleLogin} className="space-y-4">
        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

        <div>
          <label htmlFor="email" className="block mb-1 font-medium">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            placeholder="Email"
            onChange={e => setFormData({ ...formData, email: e.target.value })}
            value={formData.email}
            required
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

        <div>
          <label htmlFor="password" className="block mb-1 font-medium">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Password"
            onChange={e => setFormData({ ...formData, password: e.target.value })}
            value={formData.password}
            required
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition duration-200"
        >
          {isSubmitting ? 'Logging in...' : 'Login'}
        </button>
      </form>

      <p className="mt-4 text-center">
        Not registered yet?{' '}
        <Link to="/user-register" className="text-blue-600 hover:underline">
          Register here
        </Link>
      </p>
    </div>
  );
};

export default UserLogin;
