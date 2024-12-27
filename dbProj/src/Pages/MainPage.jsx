import { useEffect } from "react";
import Booking from "../Components/Booking";
import { DashboardSidebar } from "../Components/DashboardSidebar";
import MapWithUserLocation from "../Map";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { currPanel, currUser } from "../state";
import Dashboard from "../Components/Dashboard";

const MainPage = () => {
    const token=localStorage.getItem('token');
    const[user,setUser]=useRecoilState(currUser);
    const [panel, setPanel] = useRecoilState(currPanel);
    
    let navigate=useNavigate();
    useEffect(()=>{
        console.log(token);
        
        if(!token){
            navigate('/login');
        }
        else{
            setUser(JSON.parse(localStorage.getItem('user')));
        }
      },[]);
    return ( 
        <div className="flex w-full justify-between ">
            <DashboardSidebar/>
            {panel==2&&<Booking/>}
            {panel==1&&<Dashboard/>}
            <div className="hidden lg:block w-[60%]">
                <MapWithUserLocation mobile={false}/>
            </div>
        </div>
     );
}
 
export default MainPage;