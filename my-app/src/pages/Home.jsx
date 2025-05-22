import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => (
  <div className="text-center mt-10">
    <h1 className="text-4xl font-bold mb-8">Welcome to the Loan Application System</h1>

    <div className="mb-10">
      <h2 className="text-2xl font-semibold mb-4">User</h2>
      <Link
        to="/user-register"
        className="inline-block px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 mb-4"
      >
        Register as User
      </Link>
      <br />
      <Link
        to="/user-login"
        className="inline-block px-6 py-2 bg-yellow-600 text-white rounded hover:bg-yellow-700"
      >
        Login as User
      </Link>
       </div>
       <div><Link
        to="/confirm-loan-selection"
        className="inline-block px-6 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 mt-4"
>
          Confirm Loan Selection
      </Link></div>
       
   

    <div>
      <h2 className="text-2xl font-semibold mb-4">Agent</h2>
      <Link
        to="/register-agent"
        className="inline-block px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 mb-4"
      >
        Register as Agent
      </Link>
      <br />
      <Link
        to="/agent-login"
        className="inline-block px-6 py-2 bg-orange-600 text-white rounded hover:bg-orange-700"
      >
        Login as Agent
      </Link>
      
    </div>
  </div>
);

export default Home;
