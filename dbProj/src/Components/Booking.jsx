import { useEffect, useState } from 'react';
import { useRecoilState } from "recoil";
import { chosenLot, currPanel } from "../state";
import MapWithUserLocation from '../Map';
import useFetch from '../Hooks/useFetch';

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
  const [temp,setTemp]=useState(1)
  const { data: parkingSpots, loading: loading1, error: error1 } = useFetch('http://localhost:8080/spots/getspots', {
    method: 'GET',
    token: 'your-token-here',
    params: { lotId: lot ? lot.lotId : 0 }
  }, [lot]);

  const hours = Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'));

  // Update reserveObj dynamically
  useEffect(() => {
    if (selectedSpot && startDate && startHour && endDate && endHour) {
      const startTime = new Date(`${startDate}T${startHour}:00`)
      const endTime = new Date(`${endDate}T${endHour}:00`)
      
      setReserveObj({
        spotId: selectedSpot.spotId,
        startTime,
        endTime,
        spotType: selectedSpot.type,
        status:"ACTIVE",
        cost:0,
        penalty:0
      });
      setTemp(0)
    } else {
      setReserveObj({});
    }
  }, [selectedSpot, startDate, startHour, endDate, endHour]);

  // Conditionally trigger useFetch when booking is enabled (isBookingDisabled is false)
  const { data: beforeBook, loading: loading2, error: error2 } = useFetch(
    'http://localhost:8080/reservations/reserve', {
      method: 'POST',
      token: 'your-token-here',
      params: { reserve: false },
      data: reserveObj,
    },
    [temp]  // Trigger only when booking is not disabled
  );

  useEffect(() => {
    if (error2) {
      setSelectedSpot(null);
    }
  }, [error2]);

  return (
    <div className={`${panel === 2 ? 'w-full lg:w-[40%]' : 'w-0'} bg-white text-black overflow-hidden p-5 ease-in-out duration-300 transition-all`}>
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

        <select
          onChange={(e) => setSelectedSpotType(e.target.value)}
          value={selectedSpotType}
          className="w-full p-2 border rounded"
        >
          <option value="standard">REGULAR</option>
          <option value="electric">EV</option>
          <option value="handicap">DISABLED</option>
        </select>

        <div className='block lg:hidden'>
          <MapWithUserLocation mobile={true} />
        </div>

        {lot && parkingSpots && (
          <div className="grid grid-cols-4 gap-3 w-full">
            {parkingSpots.map((spot) => (
              <div
                key={spot.spotId}
                className={`p-2 flex justify-center relative items-center h-24 border-x-black border-x-4 cursor-pointer ${
                  selectedSpot?.spotId === spot.spotId ? 'bg-gray-200' : 'bg-white'
                } ${
                  spot.status.toLowerCase() === 'occupied' || spot.status.toLowerCase() === 'reserved'
                    ? 'bg-gray-400 cursor-not-allowed'
                    : ''
                }`}
                onClick={() => {
                  if (spot.status.toLowerCase() === 'occupied' || spot.status.toLowerCase() === 'reserved') {
                    return;
                  }
                  setSelectedSpot(spot);
                }}
              >
                <div className='absolute top-2 right-2'>
                  {spot.type === "EV" ? (
                    <i className="fa-solid fa-bolt"></i>
                  ) : spot.type === "standard" ? (
                    <i className="fa-solid fa-car"></i>
                  ) : (
                    <i className="fa-solid fa-wheelchair-move"></i>
                  )}
                </div>
                <div className="text-center">
                  <div>{spot.spotNumber}</div>
                  <div className="text-xs">{spot.status}</div>
                </div>
              </div>
            ))}
          </div>
        )}

        <button
          disabled={isBookingDisabled}
          className={`w-full p-2 rounded ${
            isBookingDisabled 
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
