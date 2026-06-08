import { Activity, Users, ShoppingBag, Package, ArrowUpRight, CheckCircle2, Clock } from 'lucide-react';
const StatCard = ({ icon: Icon, label, value, trend, color }: any) => (
  <div className="glass-card" style={{ padding: '24px' }}>
    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
      <div style={{ padding: '12px', background: `${color}20`, borderRadius: '12px', color: color }}>
        <Icon size={24} />
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', color: '#10b981', fontSize: '14px' }}>
        {trend} <ArrowUpRight size={16} />
      </div>
    </div>
    <p style={{ color: 'var(--text-muted)', fontSize: '14px', marginBottom: '4px' }}>{label}</p>
    <h3 style={{ fontSize: '24px', fontWeight: '700' }}>{value}</h3>
  </div>
);
const Dashboard = () => {
  return (
    <div className="fade-in">
      <header style={{ marginBottom: '40px' }}>
        <h1 style={{ fontSize: '32px', marginBottom: '8px' }}>System Overview</h1>
        <p style={{ color: 'var(--text-muted)' }}>Real-time health and performance metrics across all microservices.</p>
      </header>
      <div className="dashboard-grid">
        <StatCard icon={ShoppingBag} label="Total Orders" value="1,284" trend="+12.5%" color="#3b82f6" />
        <StatCard icon={Users} label="Active Users" value="452" trend="+3.2%" color="#8b5cf6" />
        <StatCard icon={Package} label="Inventory Stock" value="8,921" trend="+0.8%" color="#10b981" />
        <StatCard icon={Activity} label="System Latency" value="42ms" trend="-2.4%" color="#f59e0b" />
      </div>

      <div style={{ marginTop: '40px', display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '24px' }}>
        <div className="glass-card" style={{ padding: '24px' }}>
          <h3 style={{ marginBottom: '24px' }}>Recent Events (RabbitMQ)</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {[
              { type: 'Order Created', id: 'ORD-7721', status: 'Success', time: '2 mins ago' },
              { type: 'Payment Processed', id: 'PAY-9012', status: 'Success', time: '5 mins ago' },
              { type: 'Inventory Updated', id: 'SKU-442', status: 'Warning', time: '12 mins ago' },
              { type: 'Shipping Initiated', id: 'SHP-1102', status: 'Success', time: '15 mins ago' },
            ].map((event, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '16px', padding: '12px', background: 'rgba(255,255,255,0.03)', borderRadius: '12px' }}>
                <CheckCircle2 size={18} color={event.status === 'Success' ? '#10b981' : '#f59e0b'} />
                <div style={{ flex: 1 }}>
                  <p style={{ fontWeight: '600', fontSize: '14px' }}>{event.type}</p>
                  <p style={{ color: 'var(--text-muted)', fontSize: '12px' }}>ID: {event.id}</p>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <p style={{ fontSize: '12px', color: 'var(--text-muted)' }}>{event.time}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="glass-card" style={{ padding: '24px' }}>
          <h3 style={{ marginBottom: '24px' }}>Service Status</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {['Auth', 'Order', 'Payment', 'Inventory', 'Shipping', 'Notification'].map(service => (
              <div key={service} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: '#10b981' }}></div>
                  <span style={{ fontSize: '14px' }}>{service} Service</span>
                </div>
                <span style={{ fontSize: '12px', color: '#10b981', background: 'rgba(16, 185, 129, 0.1)', padding: '2px 8px', borderRadius: '10px' }}>Operational</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
