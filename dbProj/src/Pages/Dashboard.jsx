import { DashboardSidebar } from '../Components/DashboardSidebar';
import MapWithUserLocation from '../Map';

const Dashboard = () => {
  return (
    <div  className="flex w-full justify-between items-center">
      <DashboardSidebar />
      <MapWithUserLocation />
    </div>
  );
};

export default Dashboard;