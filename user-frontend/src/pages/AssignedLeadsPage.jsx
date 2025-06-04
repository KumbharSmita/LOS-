import React, { useState, useEffect } from 'react';
import {
  fetchAssignedLeads,
  fetchAssignedLeadsByStatus,
} from '../api/leadAssignments';
import {
  fetchDocumentsByLeadId,
  requestReupload,
} from '../api/documentApi';

const AssignedLeadsPage = () => {
  const [assignedLeads, setAssignedLeads] = useState([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [documentsData, setDocumentsData] = useState({});

  useEffect(() => {
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
    fetchLeads();
  }, [statusFilter]);

  const toggleDocuments = async (leadId) => {
    const current = documentsData[leadId];
    if (current?.isOpen) {
      setDocumentsData((prev) => ({
        ...prev,
        [leadId]: { ...current, isOpen: false },
      }));
      return;
    }

    if (!current || !current.documents) {
      setDocumentsData((prev) => ({
        ...prev,
        [leadId]: { loading: true, error: null, documents: null, isOpen: true },
      }));
      try {
        const docs = await fetchDocumentsByLeadId(leadId);
        setDocumentsData((prev) => ({
          ...prev,
          [leadId]: { loading: false, error: null, documents: docs, isOpen: true },
        }));
      } catch (err) {
        setDocumentsData((prev) => ({
          ...prev,
          [leadId]: { loading: false, error: err.message, documents: null, isOpen: true },
        }));
      }
    } else {
      setDocumentsData((prev) => ({
        ...prev,
        [leadId]: { ...current, isOpen: true },
      }));
    }
  };

  // NEW: View document inline by opening in new tab
  const viewDocument = (documentId) => {
    const url = `http://localhost:8080/api/documents/download/${documentId}`;
    window.open(url, '_blank', 'noopener,noreferrer');
  };

  const handleReuploadRequest = async (leadId, documentType) => {
    try {
      await requestReupload(leadId, documentType);
      alert(`Reupload requested for ${documentType}.`);
      const updatedDocs = await fetchDocumentsByLeadId(leadId);
      setDocumentsData((prev) => ({
        ...prev,
        [leadId]: { ...prev[leadId], documents: updatedDocs },
      }));
    } catch (error) {
      console.error('Reupload request failed:', error);
      alert('Failed to request reupload.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-10">
      <div className="max-w-4xl mx-auto bg-white shadow p-6 rounded space-y-6">
        <h1 className="text-3xl font-bold text-blue-700 mb-4">Assigned Leads</h1>

        <div className="mb-6">
          <label htmlFor="statusFilter" className="mr-2 font-semibold">
            Filter by Status:
          </label>
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
            {assignedLeads.map((assignment) => {
              const leadId = assignment.lead.leadsId;
              const docData = documentsData[leadId] || {};
              return (
                <li
                  key={assignment.lead_assignment_id}
                  className="bg-gray-50 p-4 rounded shadow mb-4"
                >
                  <p>
                    <strong>Lead ID:</strong> {leadId}
                  </p>
                  <p>
                    <strong>Name:</strong> {assignment.lead.firstName}{' '}
                    {assignment.lead.lastName}
                  </p>
                  <p>
                    <strong>Email:</strong> {assignment.lead.email}
                  </p>
                  <p>
                    <strong>Phone:</strong> {assignment.lead.phone}
                  </p>
                  <p>
                    <strong>Loan Type:</strong> {assignment.lead.loanType}
                  </p>
                  <p>
                    <strong>Amount:</strong> ₹{assignment.lead.amount}
                  </p>
                  <p>
                    <strong>Tenure:</strong> {assignment.lead.tenureMonths} months
                  </p>
                  <p>
                    <strong>Purpose:</strong> {assignment.lead.purpose}
                  </p>
                  <p>
                    <strong>Assigned At:</strong>{' '}
                    {new Date(assignment.assigned_at).toLocaleString()}
                  </p>

                  <button
                    onClick={() => toggleDocuments(leadId)}
                    className="mt-2 px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
                  >
                    {docData.isOpen ? 'Hide Documents' : 'Show Documents'}
                  </button>

                  {docData.isOpen && (
                    <div className="mt-3 p-3 bg-gray-100 rounded">
                      {docData.loading ? (
                        <p>Loading documents...</p>
                      ) : docData.error ? (
                        <p className="text-red-600">
                          Error loading documents: {docData.error}
                        </p>
                      ) : !docData.documents || docData.documents.length === 0 ? (
                        <p>No documents uploaded for this lead.</p>
                      ) : (
                        <ul className="list-disc pl-5 space-y-2">
                          {docData.documents.map((doc) => (
                            <li key={doc.documentId}>
                              <div className="flex items-center gap-3">
                                <span>
                                  {doc.documentType} - Uploaded at:{' '}
                                  {new Date(doc.uploadedAt).toLocaleString()}
                                  {doc.reuploadRequested && (
                                    <span className="ml-2 text-yellow-600 font-semibold">
                                      (Reupload Requested)
                                    </span>
                                  )}
                                </span>
                                <button
                                  onClick={() => viewDocument(doc.documentId)}
                                  className="px-2 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
                                >
                                  View
                                </button>
                                {!doc.reuploadRequested && (
                                  <button
                                    onClick={() =>
                                      handleReuploadRequest(leadId, doc.documentType)
                                    }
                                    className="px-2 py-1 bg-yellow-500 text-white rounded hover:bg-yellow-600"
                                  >
                                    Request Reupload
                                  </button>
                                )}
                              </div>
                            </li>
                          ))}
                        </ul>
                      )}
                    </div>
                  )}
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
};

export default AssignedLeadsPage;
