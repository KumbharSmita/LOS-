import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { uploadDocument, fetchDocumentsByLeadId } from '../api/documentApi';
import { fetchAssignmentByLeadId } from '../api/leadAssignments';

const DocumentUploadPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const leadsIdFromState = location.state?.leadsId || '';

  const [leadsId, setLeadsId] = useState(leadsIdFromState);
  const [documentType, setDocumentType] = useState('');
  const [file, setFile] = useState(null);
  const [uploadMessage, setUploadMessage] = useState('');
  const [documents, setDocuments] = useState([]);
  const [assignmentInfo, setAssignmentInfo] = useState(null);
  const [assignmentLoading, setAssignmentLoading] = useState(false);
  const [assignmentError, setAssignmentError] = useState('');

  useEffect(() => {
    if (leadsId) {
      fetchDocs();
    }
  }, [leadsId]);

  const fetchDocs = async () => {
    try {
      const docs = await fetchDocumentsByLeadId(leadsId);
      setDocuments(docs);
      await checkAssignmentStatus(docs);
    } catch {
      setUploadMessage('Error fetching documents.');
    }
  };

  const checkAssignmentStatus = async (docs) => {
    const types = docs.map(doc => doc.documentType.toLowerCase());
    const salarySlips = types.filter(type => type.startsWith('salary slip'));
    const hasBankStatement = types.includes('bank statement');

    if (salarySlips.length >= 3 && hasBankStatement) {
      setAssignmentLoading(true);
      setAssignmentError('');
      try {
        const assignment = await fetchAssignmentByLeadId(leadsId);
        setAssignmentInfo(assignment);

        const reuploadRequested = docs.some(doc => doc.reuploadRequested);
        if (assignment && reuploadRequested) {
          navigate('/lead-status', {
            state: { leadsId, status: 'LEAD ASSIGNED' }
          });
        }
      } catch (err) {
        setAssignmentInfo(null);
        setAssignmentError('Lead is not yet assigned to any agent.');
      } finally {
        setAssignmentLoading(false);
      }
    } else {
      setAssignmentInfo(null);
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const hasUploadedAllRequiredDocs = () => {
    const types = documents.map(doc => doc.documentType.toLowerCase());
    const salarySlips = types.filter(type => type.startsWith('salary slip'));
    const hasBankStatement = types.includes('bank statement');
    return salarySlips.length >= 3 && hasBankStatement;
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    if (!file || !leadsId || !documentType) {
      setUploadMessage('Please fill all fields.');
      return;
    }

    if (documents.some(doc => doc.documentType.toLowerCase() === documentType.toLowerCase())) {
      setUploadMessage(`You have already uploaded a ${documentType}.`);
      return;
    }

    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('leadsId', parseInt(leadsId, 10));
      formData.append('documentType', documentType);

      await uploadDocument(formData);

      setUploadMessage('Document uploaded successfully!');
      setDocumentType('');
      setFile(null);

      fetchDocs();
    } catch {
      setUploadMessage('Upload failed. Try again.');
    }
  };

  const salarySlipMonths = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const uploadedSalarySlips = documents
    .map(doc => doc.documentType.toLowerCase())
    .filter(type => type.startsWith('salary slip'));

  const availableSalarySlipOptions = salarySlipMonths.filter(month =>
    !uploadedSalarySlips.includes(`salary slip - ${month.toLowerCase()}`)
  );

  const isUploadDisabled = hasUploadedAllRequiredDocs();

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Upload Document</h2>

      <form onSubmit={handleUpload} className="space-y-4">
        {leadsId ? (
          <input
            type="text"
            value={leadsId}
            readOnly
            className="w-full p-2 border rounded bg-gray-100 cursor-not-allowed"
          />
        ) : (
          <input
            type="number"
            placeholder="Lead ID"
            value={leadsId}
            onChange={(e) => setLeadsId(e.target.value)}
            className="w-full p-2 border rounded"
            required
          />
        )}

        <select
          value={documentType}
          onChange={(e) => setDocumentType(e.target.value)}
          className="w-full p-2 border rounded"
          required
          disabled={isUploadDisabled}
        >
          <option value="">Select Document Type</option>

          {/* Salary Slip Options (max 3 months) */}
          {availableSalarySlipOptions.map(month => (
            <option key={month} value={`Salary Slip - ${month}`}>
              Salary Slip - {month}
            </option>
          ))}

          {/* Bank Statement (only if not already uploaded) */}
          {!documents.some(doc => doc.documentType.toLowerCase() === 'bank statement') && (
            <option value="Bank Statement">Bank Statement</option>
          )}
        </select>

        <input
          type="file"
          onChange={handleFileChange}
          className="w-full"
          required
          disabled={isUploadDisabled}
        />

        <button
          type="submit"
          disabled={isUploadDisabled}
          className={`w-full py-2 rounded ${
            isUploadDisabled
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-blue-600 hover:bg-blue-700 text-white'
          }`}
        >
          Upload
        </button>
      </form>

      {uploadMessage && (
        <p className="mt-4 text-center text-sm text-gray-700">{uploadMessage}</p>
      )}

      {isUploadDisabled && (
        <p className="mt-2 text-green-600 text-sm font-medium text-center">
          All required documents uploaded. Upload disabled.
        </p>
      )}

      {documents.length > 0 && (
        <div className="mt-6">
          <h3 className="font-semibold">Uploaded Documents</h3>
          <ul className="list-disc pl-5 mt-2 text-sm text-gray-700">
            {documents.map((doc) => (
              <li key={doc.documentId}>
                {doc.documentType} - Uploaded at: {new Date(doc.uploadedAt).toLocaleString()}
                {doc.reuploadRequested && (
                  <span className="ml-2 text-yellow-600 font-semibold">(Reupload Requested)</span>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}

      <div className="mt-8 p-4 border rounded bg-green-50">
        <h3 className="font-semibold mb-2">Agent Assignment Status</h3>
        {assignmentLoading && <p>Checking agent assignment...</p>}
        {assignmentError && <p className="text-red-600">{assignmentError}</p>}
        {assignmentInfo && (
          <div>
            <p>
              Assigned Agent ID: <strong>{assignmentInfo.agent_id}</strong>
            </p>
            <p>
              Assigned At: <strong>{new Date(assignmentInfo.assigned_at).toLocaleString()}</strong>
            </p>
            <p>
              Expected Contact By:{' '}
              <strong>
                {new Date(
                  new Date(assignmentInfo.assigned_at).getTime() + 2 * 60 * 60 * 1000
                ).toLocaleString()}
              </strong>
            </p>
          </div>
        )}
        {!assignmentLoading && !assignmentInfo && !assignmentError && (
          <p>Lead not assigned to any agent yet.</p>
        )}
      </div>
    </div>
  );
};

export default DocumentUploadPage;
