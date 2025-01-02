/* eslint-disable no-unused-vars */
import { useRecoilState } from "recoil";
import { changedReservations, currPanel, currUser } from "../state";
import { useEffect, useState } from "react";
import { getReservation } from "../parkUtils/parkUtils";
import ReservationCard from "./ReservationCard";

const Dashboard = () => {
    const [panel, setPanel] = useRecoilState(currPanel);
    const [reservations,setReservations]=useState([]);
    const token=localStorage.getItem('token');
    const[user,setUser]=useRecoilState(currUser);
    const [change,setChange]=useRecoilState(changedReservations);
    
    useEffect(()=>{
        async function get(){
            if(user){
            let temp=await getReservation(token,user.userId);
            setReservations(temp);
            console.log(temp);
            }
        }
        get();
    },[change]);
    return ( 
        <div 
            className={`${panel === 1 ? 'w-full lg:w-[40%] opacity-100 p-2 max-h-[calc(100vh)] overflow-y-auto' : 'opacity-0 w-0 overflow-hidden'} transition-all ease-in-out duration-300 absolute`}
        >
            <h1 className="text-3xl text-black">Reservations</h1>
            {reservations.map((reservation) => (
                <ReservationCard 
                    key={reservation.reservationId} 
                    spotNumber={reservation.spotNumber} 
                    name={reservation.lotName} 
                    cost={reservation.cost} 
                    userId={reservation.userId} 
                    penalty={reservation.penalty} 
                    status={reservation.status} 
                    startTime={reservation.startTime} 
                    endTime={reservation.endTime} 
                    spotId={reservation.spotId} 
                    reservationId={reservation.reservationId} 
                />
            ))}
        </div>
    );
    
}
 
export default Dashboard;