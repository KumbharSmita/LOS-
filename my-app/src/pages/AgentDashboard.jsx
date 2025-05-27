import React from 'react';
import { Link } from 'react-router-dom';

const AgentDashboard = () => {
  const agentId = localStorage.getItem('agent_id');
  const role = localStorage.getItem('agent_role');

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-3xl mx-auto bg-white shadow p-6 rounded space-y-6">
        <h1 className="text-3xl font-bold text-blue-700 mb-4">Welcome, Agent</h1>

        <p className="text-gray-700">
          Agent ID: <span className="font-semibold">{agentId || 'Not found'}</span>
        </p>
        <p className="text-gray-700">
          Role: <span className="font-semibold">{role || 'Not found'}</span>
        </p>

        <div className="grid gap-4 md:grid-cols-2">
          <Link
            to="/lead-status-history"
            className="block text-center bg-blue-600 text-white px-4 py-3 rounded hover:bg-blue-700"
          >
            Manage Lead Status History
          </Link>

          <Link
            to="/agent-activity-log"
            className="block text-center bg-purple-600 text-white px-4 py-3 rounded hover:bg-purple-700"
          >
            View & Log Agent Activity
          </Link>

          <Link
            to="/assigned-leads"
            className="block text-center bg-green-600 text-white px-4 py-3 rounded hover:bg-green-700"
          >
            View Assigned Leads
          </Link>

          <Link
            to="/underwriting"
            className="block text-center bg-red-600 text-white px-4 py-3 rounded hover:bg-red-700"
          >
            Perform Underwriting
          </Link>

          <Link
            to="/disbursement"
            className="block text-center bg-yellow-600 text-white px-4 py-3 rounded hover:bg-yellow-700"
          >
            Disburse Loan
          </Link>

          <Link
            to="/process-loan"
            className="inline-block px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 mb-4"
          >
            Process Loan & View EMI Schedule
          </Link>

          <Link
            to="/repayment-schedule"
            className="block text-center bg-indigo-600 text-white px-4 py-3 rounded hover:bg-indigo-700"
          >
            View Repayment Schedule
          </Link>

          {/* Show Register New Agent only for SUPER_ADMIN */}
          {role === 'SUPER_ADMIN' && (
            <Link
              to="/register-agent"
              className="block text-center bg-teal-600 text-white px-4 py-3 rounded hover:bg-teal-700"
            >
              Register New Agent
            </Link>
          )}
        </div>
      </div>
    </div>
  );
};

export default AgentDashboard;
