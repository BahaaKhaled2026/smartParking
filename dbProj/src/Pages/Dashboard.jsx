import { DashboardSidebar } from '../Components/DashboardSidebar';
import MapWithUserLocation from '../Map';
import { useNavigate } from 'react-router-dom';
import useFetch from '../hooks/useFetch';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const Dashboard = () => {

  const navigate = useNavigate();
  const { postSignout } = useFetch();

  const handleLogout = () => {
    const token = localStorage.getItem("token");
    if (!token) {
      toast.error("Session expired. Please log in again.");
      navigate("/login");
      return;
    }
    postSignout(
      "http://localhost:8080/users/signout",
      {
        token
      },
      (response, error) => {
        if(response.message ){
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          navigate("/login");
          toast.success("User signed out successfully!");
        }
        else{
          console.error(error);
          toast.error("Error signing out. Please try again.");
        }
      },
      ()=>{}
    );
  };
  return (
    <div  className="flex w-full justify-between items-center">
      <DashboardSidebar handleLogout={handleLogout} />
      <MapWithUserLocation />
    </div>
  );
};

export default Dashboard;