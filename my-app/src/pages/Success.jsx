import React from 'react';
import { useLocation } from 'react-router-dom';

function Success() {
  const location = useLocation();
  const { agentInfo, expectedContactTime, message } = location.state || {};

  if (!agentInfo || !expectedContactTime) {
    return (
      <div className="p-4 text-red-600 text-center">
        Missing success data. Please verify your OTP again.
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white">
      <h2 className="text-xl font-bold text-center mb-4">Success </h2>

      {message && <p className="text-center text-green-600 mb-6">{message}</p>}

      <div className="text-center space-y-2">
        <p>Your loan application has been successfully processed.</p>
        <p>Assigned to Agent ID: <strong>{agentInfo}</strong></p>
        <p>Expected contact by: <strong>{new Date(expectedContactTime).toLocaleString()}</strong></p>
      </div>
    </div>
  );
}

export default Success;
