import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createLead } from '../api/auth';

export default function LeadForm() {
  const navigate = useNavigate();
  const [lead, setLead] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    panNumber: '',
    aadhaarNumber: '',
    source: '',
    loanType: '',
    amount: '',
    tenureMonths: '',
    purpose: ''
  });

  const validateFields = () => {
    const { email, phone, aadhaarNumber } = lead;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) return "Invalid email format";
    if (!/^\d{10}$/.test(phone)) return "Phone must be 10 digits";
    if (!/^\d{12}$/.test(aadhaarNumber)) return "Aadhaar must be 12 digits";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const error = validateFields();
    if (error) return alert(error);

    try {
      const response = await createLead(lead);
      alert('Lead created! OTP sent to your email.');
      navigate('/verify-otp', {
        state: { leads_id: response.leads_id, email: lead.email }
      });
    } catch (error) {
      alert("Error creating lead: " + (error?.response?.data || error.message));
    }
  };

  return (
    <div className="max-w-lg mx-auto mt-10 p-6 border rounded shadow bg-white">
      <h2 className="text-xl font-bold mb-4 text-center">Lead Form</h2>
      <form onSubmit={handleSubmit}>
        {Object.entries(lead).map(([key, value]) => (
          <input
            key={key}
            type={(key === 'amount' || key === 'tenureMonths') ? 'number' : 'text'}
            placeholder={key.replace(/([A-Z])/g, ' $1')}
            value={value}
            onChange={(e) => setLead({ ...lead, [key]: e.target.value })}
            className="w-full p-2 mb-3 border rounded"
            required
          />
        ))}
        <button type="submit" className="w-full p-3 bg-blue-600 text-white rounded hover:bg-blue-700">
          Submit
        </button>
      </form>
    </div>
  );
}
