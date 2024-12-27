import React from 'react';

const ReservationCard = ({status,spotId,userId,startTime,endTime,cost,penalty,reservationId}) => {
  return (
    <div className="bg-white border-2 border-black rounded-lg p-6 max-w-lg mx-auto  mt-5">
      <h2 className="text-2xl font-bold text-center mb-4">Reservation #{reservationId}</h2>
      <div className="mb-4">
        <p><span className="font-semibold">Status:</span> {status}</p>
        <p><span className="font-semibold">Spot ID:</span> {spotId}</p>
        <p><span className="font-semibold">Start Time:</span> {new Date(startTime).toLocaleString()}</p>
        <p><span className="font-semibold">End Time:</span> {new Date(endTime).toLocaleString()}</p>
        <p><span className="font-semibold">Cost:</span> ${cost.toFixed(2)}</p>
        <p><span className="font-semibold">Penalty:</span> ${penalty.toFixed(2)}</p>
      </div>
      <div className="flex justify-between">
        <button className="bg-white text-black border border-black px-4 py-2 hover:bg-black hover:text-white">
          Edit
        </button>
        <button className="bg-black text-white px-4 py-2 hover:bg-white hover:text-black hover:border hover:border-black">
          Cancel
        </button>
        <button className="bg-white text-black border border-black px-4 py-2 hover:bg-black hover:text-white">
          View Details
        </button>
      </div>
    </div>
  );
};

export default ReservationCard;

