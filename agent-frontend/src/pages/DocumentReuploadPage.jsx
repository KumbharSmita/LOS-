import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { fetchDocumentsByLeadId, uploadDocument } from '../api/documentApi';

const DocumentReuploadPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const leadsId = location.state?.leadsId;

  const [documentsToReupload, setDocumentsToReupload] = useState([]);
  const [selectedFiles, setSelectedFiles] = useState({});
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!leadsId) {
      navigate('/document-upload');
      return;
    }
    loadReuploadDocuments();
  }, [leadsId]);

  const loadReuploadDocuments = async () => {
    try {
      const docs = await fetchDocumentsByLeadId(leadsId);
      const flaggedDocs = docs.filter(doc => doc.reuploadRequested);
      setDocumentsToReupload(flaggedDocs);

      if (flaggedDocs.length === 0) {
        setTimeout(() => navigate('/document-upload', { state: { leadsId } }), 2000);
      }
    } catch (err) {
      setErrorMessage('Failed to load documents. Try again.');
    }
  };

  const handleFileChange = (documentType, file) => {
    setSelectedFiles(prev => ({ ...prev, [documentType]: file }));
  };

  const handleReupload = async (e) => {
    e.preventDefault();
    setErrorMessage('');
    setSuccessMessage('');
    setLoading(true);

    for (const doc of documentsToReupload) {
      const file = selectedFiles[doc.documentType];
      if (!file) {
        setLoading(false);
        setErrorMessage(`Please select a file for ${doc.documentType}.`);
        return;
      }

      try {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('leadsId', parseInt(leadsId, 10));
        formData.append('documentType', doc.documentType);

        await uploadDocument(formData);
      } catch {
        setLoading(false);
        setErrorMessage(`Upload failed for ${doc.documentType}. Try again.`);
        return;
      }
    }

    // Reupload complete
    localStorage.setItem(`reuploadDoneForLead_${leadsId}`, 'true');
    setSuccessMessage('All documents reuploaded successfully! Redirecting...');
    setLoading(false);

    setTimeout(() => {
      navigate('/document-upload', { state: { leadsId } });
    }, 2000);
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Reupload Requested Documents</h2>

      {errorMessage && (
        <p className="mb-4 text-center text-red-600">{errorMessage}</p>
      )}

      {successMessage && (
        <p className="mb-4 text-center text-green-600">{successMessage}</p>
      )}

      {documentsToReupload.length === 0 && (
        <p className="text-center text-gray-600">No documents require reupload. Redirecting...</p>
      )}

      <form onSubmit={handleReupload} className="space-y-4">
        {documentsToReupload.map((doc) => (
          <div key={doc.documentId}>
            <label className="block font-semibold mb-1">{doc.documentType}</label>
            <input
              type="file"
              onChange={(e) => handleFileChange(doc.documentType, e.target.files[0])}
              required
            />
          </div>
        ))}

        {documentsToReupload.length > 0 && (
          <button
            type="submit"
            disabled={loading}
            className={`w-full py-2 rounded ${
              loading ? 'bg-gray-400 cursor-wait' : 'bg-blue-600 hover:bg-blue-700 text-white'
            }`}
          >
            {loading ? 'Uploading...' : 'Reupload Documents'}
          </button>
        )}
      </form>
    </div>
  );
};

export default DocumentReuploadPage;
