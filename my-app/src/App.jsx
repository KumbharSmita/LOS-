import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Home from './pages/Home';
import LeadForm from './pages/LeadForm';
import VerifyOtp from './pages/VerifyOtp';
import Success from './pages/Success';
import ConfirmLoanSelectionPage from './pages/ConfirmLoanSelectionPage';
import LeadStatusPage from './pages/LeadStatusPage';
import UnderwritingResultPage from './pages/UnderwritingResultPage';
import AgentRegister from './pages/AgentRegister';
import AgentLogin from './pages/AgentLogin';
import AgentDashboard from './pages/AgentDashboard';
import LeadStatusHistory from './pages/LeadStatusHistory';
import AgentActivityLog from './pages/AgentActivityLog';
import AssignedLeadsPage from './pages/AssignedLeadsPage';
import UnderwritingPage from './pages/UnderwritingPage';
import DisbursementPage from './pages/DisbursementPage';
import DisbursementStatus from './pages/DisbursementStatus';
import LoanProcessingPage from './pages/LoanProcessingPage';
import RepaymentSchedulePage from './pages/RepaymentSchedulePage';
import UserRegister from './pages/UserRegister';
import UserLogin from './pages/UserLogin';
import BankDetailsPage from './pages/BankDetailsPage';

import DocumentUpload from './pages/DocumentUploadpage';
import DocumentUploadPage from './pages/DocumentUploadpage';
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/lead-form" element={<LeadForm />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        <Route path="/success" element={<Success />} />
        <Route path="/confirm-loan-selection" element={<ConfirmLoanSelectionPage />} />
        <Route path="/lead-status" element={<LeadStatusPage />} />
        <Route path="/underwriting-result" element={<UnderwritingResultPage />} />
        <Route path="/register-agent" element={<AgentRegister />} />
        <Route path="/agent-login" element={<AgentLogin />} />
        <Route path="/agent-dashboard" element={<AgentDashboard />} />
        <Route path="/lead-status-history" element={<LeadStatusHistory />} />
        <Route path="/agent-activity-log" element={<AgentActivityLog />} />
        <Route path="/assigned-leads" element={<AssignedLeadsPage />} />
        <Route path="/underwriting" element={<UnderwritingPage />} />
        <Route path="/disbursement" element={<DisbursementPage />} />
         <Route path="/disbursement-status" element={<DisbursementStatus />} />
        <Route path="/process-loan" element={<LoanProcessingPage />} />
<Route path="/repayment-schedule" element={<RepaymentSchedulePage />} />
        <Route path="/user-register" element={<UserRegister />} />
        <Route path="/user-login" element={<UserLogin />} />
      <Route path="/bank-details" element={<BankDetailsPage />} />
        <Route path="/document-upload" element={<DocumentUploadPage />} />
      </Routes>
    </Router>
  );
}

export default App;
