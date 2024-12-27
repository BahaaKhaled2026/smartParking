import Booking from "../Components/Booking";
import { DashboardSidebar } from "../Components/DashboardSidebar";
import MapWithUserLocation from "../Map";

const MainPage = () => {
    return ( 
        <div className="flex w-full justify-between ">
            <DashboardSidebar/>
            <Booking/>
            <div className="hidden lg:block w-[60%]">
                <MapWithUserLocation mobile={false}/>
            </div>
        </div>
     );
}
 
export default MainPage;