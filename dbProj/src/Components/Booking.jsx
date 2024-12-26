import { useState } from 'react';
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

    const { data: parkingSpots, loading: loading1, error: error1 } = useFetch('http://localhost:8080/spots/getspots', {
        method: 'GET',
        token: 'your-token-here',
        params: {lotid: chosenLot.lotId}
    }, [chosenLot]);
    // const parkingSpots = [
    //   { spotId: '1', spotNumber: 1, status: 'available', type: 'standard' },
    //   { spotId: '2', spotNumber: 2, status: 'occupied', type: 'disabled' },
    //   { spotId: '3', spotNumber: 3, status: 'available', type: 'EV' },
    //   { spotId: '4', spotNumber: 4, status: 'available', type: 'standard' },
    //   { spotId: '5', spotNumber: 1, status: 'available', type: 'standard' },
    //   { spotId: '6', spotNumber: 2, status: 'occupied', type: 'disabled' },
    //   { spotId: '7', spotNumber: 3, status: 'available', type: 'EV' },
    //   { spotId: '8', spotNumber: 4, status: 'available', type: 'standard' },
    //   { spotId: '9', spotNumber: 1, status: 'available', type: 'standard' },
    //   { spotId: '10', spotNumber: 2, status: 'occupied', type: 'disabled' },
    //   { spotId: '11', spotNumber: 3, status: 'available', type: 'EV' },
    //   { spotId: '12', spotNumber: 4, status: 'reserved', type: 'standard' },
    // ];

    const handleBook = () => {
      if (selectedSpot && startDate && startHour && endDate && endHour) {
        const bookingObject = {
          spotId: selectedSpot.spotId,
          startTime: `${startDate}T${startHour}:00`,
          endTime: `${endDate}T${endHour}:00`,
          spotType: selectedSpot.type
        };
        console.log('Booking object:', bookingObject);
      }
    };

    const isBookingDisabled = !selectedSpot || !startDate || !startHour || !endDate || !endHour;

    const hours = Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'));

    return ( 
        <div className={`${panel == 2 ? 'w-full lg:w-[40%]' : 'w-0'}  overflow-hidden p-5 ease-in-out duration-300 transition-all`}>
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
                <MapWithUserLocation mobile={true}/>
              </div>

              {lot && (
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
                onClick={handleBook} 
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
}
 
export default Booking;

