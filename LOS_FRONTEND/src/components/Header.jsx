import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header className="bg-blue-600 text-white p-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">Loan Origination System</h1>
      <nav>
        <ul className="flex space-x-4">
          <li>
            <Link to="/random1" className="hover:underline">Random Page 1</Link>
          </li>
          <li>
            <Link to="/random2" className="hover:underline">Random Page 2</Link>
          </li>
          <li>
            <Link to="/leads" className="hover:underline">Leads</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
