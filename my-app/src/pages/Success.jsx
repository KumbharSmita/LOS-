import React from 'react';
import { Link } from 'react-router-dom';

export default function Success() {
  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white text-center">
      <h2 className="text-2xl font-bold text-green-600 mb-4">🎉 OTP Verified Successfully!</h2>
      <p className="mb-6">Thank you! Your loan lead has been verified.</p>
      <Link to="/" className="text-blue-600 underline">Go back to Lead Form</Link>
    </div>
  );
}
