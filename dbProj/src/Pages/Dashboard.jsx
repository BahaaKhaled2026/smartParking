/* eslint-disable no-unused-vars */
import React from 'react';
import { DashboardSidebar } from '../Components/DashboardSidebar';
import MapWithUserLocation from '../Map';
import { useNavigate } from 'react-router-dom';
import useFetch from '../Hooks/useFetch';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Dashboard = () => {
  const navigate = useNavigate();
  const { postSignout } = useFetch();

  const handleLogout = () => {
    console.log("Logging out...");
    const token = localStorage.getItem("token");
    console.log(token);
    if (!token) {
      toast.error("Session expired. Please log in again.");
      navigate("/login");
      return;
    }
    postSignout(
      "http://localhost:8080/users/signOut",
      {
        token
      },
      (response, error) => {
        console.log(response);
        if(response && response.message) {
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          navigate("/login");
          toast.success("User signed out successfully!");
        } else {
          console.error(error);
          toast.error("Error signing out. Please try again.");
        }
      },
      ()=>{}
    );
  };

  return (
    <div className="flex w-full justify-between items-center">
      <DashboardSidebar handleLogout={handleLogout} />
      <MapWithUserLocation />
    </div>
  );
};

export default Dashboard;