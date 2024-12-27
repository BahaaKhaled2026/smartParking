/* eslint-disable no-unused-vars */
import { useEffect } from "react";
import Booking from "../Components/Booking";
import { DashboardSidebar } from "../Components/DashboardSidebar";
import MapWithUserLocation from "../Map";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { currPanel, currUser } from "../state";
import Dashboard from "../Components/Dashboard";
import WebSocketComponent from "../Components/WebSocketComponent";
import useFetch from '../Hooks/useFetch';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const MainPage = () => {
    const token=localStorage.getItem('token');
    const[user,setUser]=useRecoilState(currUser);
    const [panel, setPanel] = useRecoilState(currPanel);
    const navigate = useNavigate();
    const { postSignout } = useFetch();

    // let navigate=useNavigate();
    useEffect(()=>{
        console.log(token);
        
        if(!token){
            navigate('/login');
        }
        else{
            setUser(JSON.parse(localStorage.getItem('user')));
        }
      },[]);

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
        <div className="flex w-full justify-between ">
            <DashboardSidebar handleLogout={handleLogout}/>
            {panel==2&&<Booking/>}
            {panel==1&&<Dashboard/>}
            {panel==6&&<WebSocketComponent/>}
            <div className="hidden lg:block w-[60%]">
                <MapWithUserLocation mobile={false}/>
            </div>
        </div>
     );
}
 
export default MainPage;