import React, { useState, useEffect } from 'react';
import { fetchAssignedLeads, fetchAssignedLeadsByStatus } from '../api/leadAssignments';

const AssignedLeadsPage = () => {
  const [assignedLeads, setAssignedLeads] = useState([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchLeads = async () => {
    setLoading(true);
    try {
      const agentId = localStorage.getItem('agent_id');
      const leads = statusFilter
        ? await fetchAssignedLeadsByStatus(agentId, statusFilter)
        : await fetchAssignedLeads(agentId);

      setAssignedLeads(leads);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeads();
  }, [statusFilter]);

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-4xl mx-auto bg-white shadow p-6 rounded space-y-6">
        <h1 className="text-3xl font-bold text-blue-700 mb-4">Assigned Leads</h1>

        <div className="mb-6">
          <label htmlFor="statusFilter" className="mr-2 font-semibold">Filter by Status:</label>
          <select
            id="statusFilter"
            className="p-2 border rounded"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">All</option>
            <option value="APPROVED">APPROVED</option>
            <option value="CONDITIONAL">CONDITIONAL</option>
            <option value="REJECTED">REJECTED</option>
            <option value="Confirmed by Borrower">Confirmed by Borrower</option>
          </select>
        </div>

        {loading ? (
          <p>Loading leads...</p>
        ) : error ? (
          <p className="text-red-600">Error fetching leads: {error}</p>
        ) : assignedLeads.length === 0 ? (
          <p>No leads found.</p>
        ) : (
          <ul>
            {assignedLeads.map((assignment) => (
              <li key={assignment.lead_assignment_id} className="bg-gray-50 p-4 rounded shadow mb-4">
                <p><strong>Lead ID:</strong> {assignment.lead.leadsId}</p>
                <p><strong>Name:</strong> {assignment.lead.firstName} {assignment.lead.lastName}</p>
                <p><strong>Email:</strong> {assignment.lead.email}</p>
                <p><strong>Phone:</strong> {assignment.lead.phone}</p>
                <p><strong>Loan Type:</strong> {assignment.lead.loanType}</p>
                <p><strong>Amount:</strong> ₹{assignment.lead.amount}</p>
                <p><strong>Tenure:</strong> {assignment.lead.tenureMonths} months</p>
                <p><strong>Purpose:</strong> {assignment.lead.purpose}</p>
              
                <p><strong>Assigned At:</strong> {new Date(assignment.assigned_at).toLocaleString()}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default AssignedLeadsPage;
