import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../api/userApi';

const UserLogin = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [isSubmitting, setIsSubmitting] = useState(false); // Track if the form is being submitted
  const [errorMessage, setErrorMessage] = useState(''); // For handling backend errors
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsSubmitting(true); // Disable button when submitting
    setErrorMessage(''); // Reset any previous error message

    try {
      const response = await loginUser(formData);
      alert(response.data); // e.g., "Login successful for: email"
      localStorage.setItem('userEmail', formData.email);
      navigate('/lead-form');
    } catch (err) {
      console.error(err);
      setErrorMessage('Login failed. Please check your credentials and try again.');
    } finally {
      setIsSubmitting(false); // Re-enable the button
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center text-blue-700">User Login</h2>

      <form onSubmit={handleLogin} className="space-y-4">
        {/* Error message */}
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
    </div>
  );
};

export default UserLogin;
