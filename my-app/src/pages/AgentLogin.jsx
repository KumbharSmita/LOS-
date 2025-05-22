import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode'; // Make sure you have jwt-decode installed
import api from '../api/axios';  // Make sure your axios instance is set up

const AgentLogin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false); // For loading state

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); // Start loading state
    setMessage(''); // Clear any previous messages

    try {
      const response = await api.post('/agent/login', formData); // Make sure the URL is correct

      const { token, agent_id, email } = response.data;

      if (token) {
        // Decode the token (optional)
        const decoded = jwtDecode(token);
        console.log('Decoded Token:', decoded);

        // Save token and user details to localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('agent_id', agent_id);  // Save the agent_id from the response
        localStorage.setItem('agent_email', email);  // Save the email from the response
        localStorage.setItem('agent_role', decoded.role);  // Save role from decoded token

        // Provide feedback to user
        setMessage('Login successful!');

        // Redirect user to dashboard after successful login
        setTimeout(() => {
          navigate('/agent-dashboard');
        }, 1500); // Slight delay to show success message
      } else {
        setMessage('Login failed: Token not found.');
      }
    } catch (error) {
      // Handle errors
      console.error('Login error:', error);
      setMessage('Login failed: ' + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false); // End loading state
    }
  };

  return (
    <div className="max-w-md mx-auto mt-12 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center">Agent Login</h2>

      {message && (
        <div
          className={`mb-4 text-sm px-3 py-2 rounded ${message.includes('successful') ? 'text-green-600 bg-green-100' : 'text-red-600 bg-red-100'}`}
        >
          {message}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
          className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
          className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring focus:ring-blue-300"
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
};

export default AgentLogin;
