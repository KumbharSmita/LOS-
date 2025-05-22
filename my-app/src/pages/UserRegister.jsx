import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../api/userApi';

const UserRegister = () => {
  const [formData, setFormData] = useState({
    full_name: '',
    contact_no: '',
    email: '',
    password: ''
  });

  const [isSubmitting, setIsSubmitting] = useState(false); // Track if the form is being submitted
  const [errorMessage, setErrorMessage] = useState(''); // For handling backend errors
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true); // Disable button when submitting
    setErrorMessage(''); // Reset any previous error message

    try {
      await registerUser(formData);
      alert('Registration successful!');
      navigate('/lead-form');
    } catch (err) {
      console.error(err);
      setErrorMessage('Registration failed. Please try again.');
    } finally {
      setIsSubmitting(false); // Re-enable the button
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center text-blue-700">User Registration</h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Error message */}
        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

        <div>
          <label htmlFor="full_name" className="block mb-1 font-medium">Full Name</label>
          <input
            type="text"
            id="full_name"
            name="full_name"
            placeholder="Full Name"
            onChange={e => setFormData({ ...formData, full_name: e.target.value })}
            value={formData.full_name}
            required
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

        <div>
          <label htmlFor="contact_no" className="block mb-1 font-medium">Contact Number</label>
          <input
            type="text"
            id="contact_no"
            name="contact_no"
            placeholder="Contact Number"
            onChange={e => setFormData({ ...formData, contact_no: e.target.value })}
            value={formData.contact_no}
            required
            className="w-full border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>

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
          {isSubmitting ? 'Submitting...' : 'Register'}
        </button>
      </form>
    </div>
  );
};

export default UserRegister;
