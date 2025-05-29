import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { uploadDocument, fetchDocumentsByLeadId } from '../api/documentApi';

const DocumentUploadPage = () => {
  const location = useLocation();
  const leadsIdFromState = location.state?.leadsId || '';

  const [leadsId, setLeadsId] = useState(leadsIdFromState);
  const [documentType, setDocumentType] = useState('');
  const [file, setFile] = useState(null);
  const [uploadMessage, setUploadMessage] = useState('');
  const [documents, setDocuments] = useState([]);

  useEffect(() => {
    if (leadsId) {
      fetchDocs();
    }
  }, [leadsId]);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    if (!file || !leadsId || !documentType) {
      setUploadMessage('Please fill all fields.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('leadsId', leadsId);
    formData.append('documentType', documentType);

    try {
      await uploadDocument(formData);
      setUploadMessage('Document uploaded successfully!');
      setDocumentType('');
      setFile(null);
      fetchDocs();
    } catch (error) {
      setUploadMessage('Upload failed. Try again.');
    }
  };

  const fetchDocs = async () => {
    try {
      const docs = await fetchDocumentsByLeadId(leadsId);
      setDocuments(docs);
    } catch (err) {
      setUploadMessage('Error fetching documents.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Upload Document</h2>
      <form onSubmit={handleUpload} className="space-y-4">
        {/* Show Lead ID as read-only if passed from state */}
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
        >
          <option value="">Select Document Type</option>
          <option value="Salary Slip">Salary Slip</option>
          <option value="Bank Statement">Bank Statement</option>
        </select>

        <input type="file" onChange={handleFileChange} className="w-full" required />

        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Upload
        </button>
      </form>

      {uploadMessage && <p className="mt-4 text-center text-sm">{uploadMessage}</p>}

      {documents.length > 0 && (
        <div className="mt-6">
          <h3 className="font-semibold">Uploaded Documents</h3>
          <ul className="list-disc pl-5 mt-2 text-sm text-gray-700">
            {documents.map((doc) => (
              <li key={doc.documentId}>
                {doc.documentType} - Uploaded at: {new Date(doc.uploadedAt).toLocaleString()}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default DocumentUploadPage;
