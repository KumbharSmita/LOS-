import { Routes, Route } from 'react-router-dom';
import RandomPage1 from '../pages/RandomPage1';
import RandomPage2 from '../pages/RandomPage2';
import LeadsPage from '../pages/LeadsPage';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/random1" element={<RandomPage1 />} />
      <Route path="/random2" element={<RandomPage2 />} />
      <Route path="/leads" element={<LeadsPage />} />
    </Routes>
  );
}
export default AppRoutes;
