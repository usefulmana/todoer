// src/App.jsx
import { BrowserRouter as Router, Routes, Route, Outlet } from 'react-router-dom';
import { Box } from '@chakra-ui/react';
import Navbar from './components/Navbar';
import LandingPage from './pages/Landing';
import Auth from './pages/Auth';

// Layout Component
const Layout = () => {
  return (
    <Box minH="100vh">
      <Navbar />
      <Outlet />
    </Box>
  );
};

// Main App component with routing
const App = () => {
  return (
    <Router>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<LandingPage />} />
          <Route path="/auth" element={<Auth />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default App;