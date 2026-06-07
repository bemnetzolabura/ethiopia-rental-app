import { LayoutDashboard, ShoppingCart, Box, Truck, Bell, Settings, LogOut, User } from 'lucide-react';
import { NavLink } from 'react-router-dom';

const Sidebar = () => {
  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/' },
    { icon: ShoppingCart, label: 'Orders', path: '/orders' },
    { icon: Box, label: 'Inventory', path: '/inventory' },
    { icon: Truck, label: 'Shipping', path: '/shipping' },
    { icon: Bell, label: 'Notifications', path: '/notifications' },
  ];

  return (
    <aside className="sidebar glass">
      <div className="logo-container">
        <h2 className="gradient-text" style={{ fontSize: '1.5rem', marginBottom: '8px' }}>MicroNexus</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>Event-Driven Ecosystem</p>
      </div>

      <nav style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '8px' }}>
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => `btn-ghost ${isActive ? 'active-nav' : ''}`}
            style={{ 
              textDecoration: 'none', 
              justifyContent: 'flex-start',
              padding: '12px 16px',
              width: '100%'
            }}
          >
            <item.icon size={20} />
            <span>{item.label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer" style={{ borderTop: '1px solid var(--glass-border)', paddingTop: '24px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
        <button className="btn-ghost" style={{ justifyContent: 'flex-start' }}>
          <User size={20} />
          <span>Profile</span>
        </button>
        <button className="btn-ghost" style={{ justifyContent: 'flex-start', color: 'var(--danger)' }}>
          <LogOut size={20} />
          <span>Logout</span>
        </button>
      </div>
      
      <style>{`
        .active-nav {
          background: rgba(59, 130, 246, 0.1) !important;
          color: var(--primary) !important;
          border-color: var(--primary) !important;
        }
      `}</style>
    </aside>
  );
};

export default Sidebar;
