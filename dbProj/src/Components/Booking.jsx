import { useEffect, useState } from 'react';
import { useRecoilState } from "recoil";
import { chosenLot, currPanel, currUser } from "../state";
import MapWithUserLocation from '../Map';
import useFetch from '../Hooks/useFetch';
import { reserveSpot } from '../parkUtils/parkUtils';
import useFetch2 from '../Hooks/useFetch2';
import { useNavigate } from 'react-router-dom';

const Booking = () => {
  const [panel, setPanel] = useRecoilState(currPanel);
  const [lot, setLot] = useRecoilState(chosenLot);
  const [startDate, setStartDate] = useState('');
  const [startHour, setStartHour] = useState('');
  const [endDate, setEndDate] = useState('');
  const [endHour, setEndHour] = useState('');
  const [selectedSpotType, setSelectedSpotType] = useState('');
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [reserveObj, setReserveObj] = useState({});
  const isBookingDisabled = !selectedSpot || !startDate || !startHour || !endDate || !endHour;
  const [price,setPrice]=useState(0);
  const token=localStorage.getItem('token');
  const[user,setUser]=useRecoilState(currUser);
  const[error,setError]=useState("");
  const [closeButton,setCloseButton]=useState(true);
  const { data: parkingSpots, loading: loading1, error: error1 } = useFetch2('http://localhost:8080/spots/getspots', {
    method: 'GET',
    token: token,
    params: { lotId: lot ? lot.lotId : 0 }
  }, [lot]);
  const navigate=useNavigate();
  useEffect(()=>{
    
    if(!token){
        navigate('/')
    }
    else{
        setUser(JSON.parse(localStorage.getItem('user')));
    }
  },[]);

  const hours = Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'));
  useEffect(() => {
    async function up(){
        if (selectedSpot && startDate && startHour && endDate && endHour) {
            const startTime = new Date(`${startDate}T${startHour}:00`).toISOString();
            const endTime = new Date(`${endDate}T${endHour}:00`).toISOString();
            const obj = {
              userId: user.userId,
              spotId: selectedSpot.spotId,
              spotNumber:selectedSpot.spotNumber,
              startTime,
              endTime,
              spotType: selectedSpot.type,
              status: "ACTIVE",
              cost: 0,
              penalty: 0,
              lotId:lot.lotId,
              lotName:lot.name
            };
            
            setCloseButton(false)
            setReserveObj(obj);
            let x=await reserveSpot(token, obj, false);
            if(x.NotValid){
              setError(x.errorMsg);
              setCloseButton(true);
              return
            }
            console.log(x);
            setError("");
            setPrice(x.cost);
          } else {
            setReserveObj({});
          }
          
    }
    up();
  }, [selectedSpot, startDate, startHour, endDate, endHour]);
  async function reserveSubmit(){
    console.log(reserveObj);
    
    let temp=await reserveSpot(token,reserveObj,true);
    console.log(temp);
    
  }


  return (
    <div className={`${panel === 2 ? 'w-full lg:w-[40%] p-5' : 'w-0'} bg-white text-black overflow-hidden  ease-in-out duration-300 transition-all`}>
      <h1 className="text-3xl text-black mb-4">BOOK YOUR SPOT</h1>
      <div className="space-y-4">
        <div className="flex space-x-2">
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="w-2/3 p-2 border rounded"
          />
          <select
            value={startHour}
            onChange={(e) => setStartHour(e.target.value)}
            className="w-1/3 p-2 border rounded"
          >
            <option value="">Hour</option>
            {hours.map(hour => (
              <option key={hour} value={hour}>{hour}:00</option>
            ))}
          </select>
          
        </div>
        
        <div className="flex space-x-2">
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="w-2/3 p-2 border rounded"
          />
          <select
            value={endHour}
            onChange={(e) => setEndHour(e.target.value)}
            className="w-1/3 p-2 border rounded"
          >
            <option value="">Hour</option>
            {hours.map(hour => (
              <option key={hour} value={hour}>{hour}:00</option>
            ))}
          </select>
        </div>

        <h2>price: ${price}</h2>
        <h1 className='text-red-600'>{error}</h1>
        <div className='block lg:hidden'>
          <MapWithUserLocation mobile={true} />
        </div>

        {lot && parkingSpots && (
  <div className="grid grid-cols-4 gap-4 p-4 bg-gray-100 border-2 rounded-lg">
    {parkingSpots.map((spot) => (
        <div
            key={spot.spotId}
            className={`relative flex flex-col items-center justify-center h-28 w-full p-3 border rounded-lg 
            ${
                selectedSpot?.spotId === spot.spotId
                ? 'bg-green-200'
                : spot.status.toLowerCase() === 'occupied' || spot.status.toLowerCase() === 'reserved'
                ? 'bg-gray-300 cursor-not-allowed'
                : 'bg-white hover:bg-blue-100 cursor-pointer'
            }`}
            onClick={() => {
            if (spot.status.toLowerCase() === 'occupied' || spot.status.toLowerCase() === 'reserved') {
                return;
            }
            setSelectedSpotType(spot.type);
            setSelectedSpot(spot);
            }}
        >
            <div className="absolute top-2 right-2 text-xl">
                {spot.type === 'EV' ? (
                    <i className="fa-solid fa-bolt text-yellow-500"></i>
                ) : spot.type === 'REGULAR' ? (
                    <i className="fa-solid fa-car text-gray-600"></i>
                ) : (
                    <i className="fa-solid fa-wheelchair-move text-blue-500"></i>
                )}
                </div>
                <div className="text-center">
                <div className="text-lg font-bold">{spot.spotNumber}</div>
                <div
                    className={`text-[10px] md:text-sm ${
                    spot.status.toLowerCase() === 'occupied'
                        ? 'text-red-500'
                        : spot.status.toLowerCase() === 'reserved'
                        ? 'text-yellow-500'
                        : 'text-green-500'
                    }`}
                >
                    {spot.status}
                </div>
                </div>
            </div>
            ))}
        </div>
    )}


        <button
            onClick={reserveSubmit}
            disabled={isBookingDisabled || closeButton}
            className={`w-full p-2 rounded ${
                isBookingDisabled || closeButton
                ? 'bg-gray-300 cursor-not-allowed' 
                : 'bg-blue-500 text-white hover:bg-blue-600'
            }`}
        >
          Book Now
        </button>
      </div>
    </div>
  );
};

export default Booking;
