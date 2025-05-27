import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

function UnderwritingResultPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const {
    leadsId,
    decision,
    riskRating,
    approvedAmount,
    underwriterNotes,
    evaluatedAt
  } = location.state || {};

  const [isConfirmed, setIsConfirmed] = useState(false);

  const decisionLower = decision?.toLowerCase() || '';
  const isApproved = ['approved', 'conditional'].includes(decisionLower);

  const handleConfirmClick = () => {
    // Simulate confirmation logic or replace with API call if needed
    setIsConfirmed(true);

    // Optional: navigate to another page
    // navigate('/confirm-loan-selection', {
    //   state: { leadId: leadsId, approvedAmount }
    // });
  };

  if (!leadsId || !decision) {
    return (
      <div className="p-4 text-red-600 text-center">
        No underwriting result available. Please try again later.
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto mt-20 p-6 border rounded shadow bg-white">
      <h2 className="text-xl font-bold mb-4 text-center text-blue-700">Underwriting Result</h2>

      <div className="space-y-2 text-gray-800">
        <p><strong>Lead ID:</strong> {leadsId}</p>
        <p><strong>Decision:</strong> {decision}</p>
        <p><strong>Risk Rating:</strong> {riskRating}</p>
        <p><strong>Approved Amount:</strong> ₹{approvedAmount}</p>
        <p><strong>Notes:</strong> {underwriterNotes}</p>
        <p><strong>Evaluated At:</strong> {new Date(evaluatedAt).toLocaleString()}</p>
      </div>

      {isApproved ? (
        isConfirmed ? (
          <p className="mt-6 text-center text-green-700 font-semibold">
            Your loan selection has been confirmed successfully. Thank you!<br />
            We will notify you once the next steps are ready.
          </p>
        ) : (
          <div className="mt-6 text-center">
            <button
              onClick={handleConfirmClick}
              className="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
            >
              Confirm Loan Selection
            </button>
          </div>
        )
      ) : (
        <p className="mt-6 text-center text-red-600 font-medium">
          Unfortunately, your loan application was not approved.<br />
          Please contact support or try again later.
        </p>
      )}
    </div>
  );
}

export default UnderwritingResultPage;
