import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => (
  <div className="text-center mt-10">
    <h1 className="text-4xl font-bold mb-8">Welcome to the Loan Application System</h1>

    <div className="mb-10">
     
     
      <br />
      <Link
        to="/user-login"
        className="inline-block px-6 py-2 bg-yellow-600 text-white rounded hover:bg-yellow-700"
      >
        Login as User
      </Link>
       </div>
      
       
   

    
  </div>
);

export default Home;
