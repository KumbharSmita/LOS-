import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { getRepaymentSchedule } from '../api/repayment';

const DisbursementStatus = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { disbursement, emiSchedule } = location.state || {};

  const [repaymentSchedule, setRepaymentSchedule] = useState(null);
  const [showEmiSchedule, setShowEmiSchedule] = useState(false);
  const [showRepaymentSchedule, setShowRepaymentSchedule] = useState(false);

  useEffect(() => {
    if (!disbursement) {
      navigate('/user-login');
    }
  }, [disbursement, navigate]);

  useEffect(() => {
    const fetchRepaymentSchedule = async () => {
      try {
        const schedule = await getRepaymentSchedule(disbursement.leadsId);
        setRepaymentSchedule(schedule);
      } catch {
        setRepaymentSchedule(null);
      }
    };

    if (showRepaymentSchedule && !repaymentSchedule) {
      fetchRepaymentSchedule();
    }
  }, [showRepaymentSchedule, disbursement, repaymentSchedule]);

  if (!disbursement) return null;

  return (
    <div className="max-w-lg mx-auto mt-10 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center text-green-700">
        Loan Disbursement Status
      </h2>

      <div className="space-y-3 text-gray-800">
        <p><strong>Leads Id:</strong> {disbursement.leadsId}</p>
        <p><strong>Approved Amount:</strong> ₹{disbursement.approvedAmount}</p>
       
        <p><strong>Processing Fee:</strong> ₹{disbursement.processingFee}</p>
        <p><strong>Disbursed Amount:</strong> ₹{disbursement.disbursedAmount}</p>
        <p><strong>Bank Account:</strong>{disbursement.bankAccount}</p>
        <p><strong>UTR Number:</strong> {disbursement.utrNumber}</p>
        <p><strong>Status:</strong> {disbursement.status}</p>
        <p>
          <strong>Disbursed At:</strong>{' '}
          {disbursement.disbursedAt ? new Date(disbursement.disbursedAt).toLocaleString() : 'N/A'}
        </p>
      </div>

      {/* EMI Schedule Section */}
      <div className="mt-8">
        <button
          onClick={() => setShowEmiSchedule((prev) => !prev)}
          className="mb-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          {showEmiSchedule ? 'Hide EMI Schedule' : 'Show EMI Schedule'}
        </button>

        {showEmiSchedule && (
          Array.isArray(emiSchedule) && emiSchedule.length > 0 ? (
            <table className="w-full border-collapse border border-gray-300 text-sm">
              <thead>
                <tr>
                  <th className="border border-gray-300 p-2">EMI ID</th>
                  <th className="border border-gray-300 p-2">Due Date</th>
                  <th className="border border-gray-300 p-2">EMI Amount</th>
                  <th className="border border-gray-300 p-2">Paid Date</th>
                  <th className="border border-gray-300 p-2">Status</th>
                </tr>
              </thead>
              <tbody>
                {emiSchedule.map((emi) => (
                  <tr key={emi.emi_id}>
                    <td className="border border-gray-300 p-2">{emi.emi_id}</td>
                    <td className="border border-gray-300 p-2">{emi.due_date}</td>
                    <td className="border border-gray-300 p-2">₹{emi.emi_amount}</td>
                    <td className="border border-gray-300 p-2">{emi.paid_date || '-'}</td>
                    <td className="border border-gray-300 p-2">{emi.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p className="text-gray-600">EMI schedule not available.</p>
          )
        )}
      </div>

      {/* Repayment Schedule Section */}
      <div className="mt-10">
        <button
          onClick={() => setShowRepaymentSchedule((prev) => !prev)}
          className="mb-4 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
        >
          {showRepaymentSchedule ? 'Hide Repayment Schedule Details' : 'Show Repayment Schedule Details'}
        </button>

        {showRepaymentSchedule && (
          repaymentSchedule ? (
            <table className="table-auto w-full text-sm border-collapse border border-gray-300">
              <tbody>
                {Object.entries(repaymentSchedule).map(([key, value]) => (
                  <tr key={key} className="border-b border-gray-300">
                    <td className="border border-gray-300 p-2 font-medium capitalize">{key}</td>
                    <td className="border border-gray-300 p-2">{String(value)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p className="text-gray-600">Repayment schedule not available.</p>
          )
        )}
      </div>

      <button
        onClick={() => navigate('/user-login')}
        className="mt-6 w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
      >
        Back to Dashboard
      </button>
    </div>
  );
};

export default DisbursementStatus;
