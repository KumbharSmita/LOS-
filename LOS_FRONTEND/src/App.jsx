import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';
import Header from './components/Header';
import Footer from './components/Footer';

function App() {
  return (
    <Router>
      <Header />
      <main className="min-h-screen">
        <AppRoutes />
      </main>
      <Footer />
    </Router>
  );
}

export default App;
