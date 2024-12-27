import { useRecoilState } from "recoil";
import { currPanel, currUser } from "../state";
import { useEffect, useState } from "react";
import { getReservation } from "../parkUtils/parkUtils";
import ReservationCard from "./ReservationCard";

const Dashboard = () => {
    const [panel, setPanel] = useRecoilState(currPanel);
    const [reservations,setReservations]=useState([]);
    const token=localStorage.getItem('token');
    const[user,setUser]=useRecoilState(currUser);
    useEffect(()=>{
        async function get(){
            let temp=await getReservation(token,user.userId);
            setReservations(temp);
            console.log(temp);
        }
        get();
    },[])
    return ( 
        <div className={`${panel === 1 ? 'w-full lg:w-[40%]' : 'w-0'}`}>
            {reservations.map((reservation)=>(
                <ReservationCard cost={reservation.cost} userId={reservation.userId} penalty={reservation.penalty} status={reservation.status} startTime={reservation.startTime} endTime={reservation.endTime} spotId={reservation.spotId} reservationId={reservation.reservationId} />
            ))}
        </div>
     );
}
 
export default Dashboard;