import React from 'react';
import { useLocation } from 'react-router-dom';

function LeadStatusPage() {
  const location = useLocation();
  const { leadsId, status } = location.state || {};

  if (!leadsId) {
    return (
      <div className="p-4 text-red-600 text-center">
        Missing lead status data.
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white text-center">
      <h2 className="text-xl font-bold mb-4">Lead Status</h2>
      <p>Your application with Lead ID: <strong>{leadsId}</strong> is currently <strong>{status}</strong>.</p>
      <p>Please wait while we process your application. You will be contacted soon.</p>
    </div>
  );
}

export default LeadStatusPage;
