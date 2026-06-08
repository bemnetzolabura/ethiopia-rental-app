import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
function App() {
  return (
    <Router>
      <div className="app-container">
        <Sidebar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/orders" element={<div className="fade-in"><h1>Orders</h1><p style={{color:'var(--text-muted)'}}>Manage your customer orders here.</p></div>} />
            <Route path="/inventory" element={<div className="fade-in"><h1>Inventory</h1><p style={{color:'var(--text-muted)'}}>Monitor stock levels and warehouse data.</p></div>} />
            <Route path="/shipping" element={<div className="fade-in"><h1>Shipping</h1><p style={{color:'var(--text-muted)'}}>Track shipments and delivery status.</p></div>} />
            <Route path="/notifications" element={<div className="fade-in"><h1>Notifications</h1><p style={{color:'var(--text-muted)'}}>System alerts and event logs.</p></div>} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}
export default App;
