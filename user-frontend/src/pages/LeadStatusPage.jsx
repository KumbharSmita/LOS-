import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import RejectedPage from './RejectedPage';

function LeadStatusPage() {
  const location = useLocation();
  const { leadsId } = location.state || {};
  const [loading, setLoading] = useState(true);
  const [leadStatus, setLeadStatus] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!leadsId) {
      setError('Missing lead ID.');
      setLoading(false);
      return;
    }

    // Fetch lead status from backend
    fetch(`http://localhost:8080/api/leads/${leadsId}/status`)
      .then(res => {
        if (!res.ok) {
          throw new Error('Failed to fetch lead status');
        }
        return res.json();
      })
      .then(data => {
        setLeadStatus(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, [leadsId]);

  if (loading) {
    return <div className="p-4 text-center">Loading lead status...</div>;
  }

  if (error) {
    return <div className="p-4 text-red-600 text-center">{error}</div>;
  }

  // If lead status is REJECTED, show RejectedPage
  if (leadStatus?.status === 'REJECTED') {
    return <RejectedPage />;
  }

  // Otherwise, show regular lead status info
  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white text-center">
      <h2 className="text-xl font-bold mb-4">Lead Status</h2>
      <p>
        Your application with Lead ID: <strong>{leadStatus.leadId}</strong> is currently <strong>{leadStatus.status}</strong>.
      </p>
      <p>Please wait while we process your application. You will be contacted soon.</p>
    </div>
  );
}

export default LeadStatusPage;
