/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from 'react';
import { useRecoilState } from 'recoil';
import { changedLots } from '../state';

const ManagedLotCard = ({ lotId, lotName }) => {
  const [change, setChange] = useRecoilState(changedLots);
  const token = localStorage.getItem('token');

  console.log(lotId);
  console.log(lotName);

  const handleDownload = () => {
    console.log("Downloading...");
    // Implement download functionality here
  };

  return (
    <div className="bg-white border-2 border-black rounded-lg p-6 max-w-lg mx-auto mt-5">
      <h2 className="text-2xl font-bold text-center mb-4">Lot #{lotId}</h2>
      <h3 className="text-xl font-bold text-center mb-4">{lotName}</h3>
      <button
        onClick={handleDownload}
        className="bg-black text-white px-4 py-2 hover:bg-white hover:text-black hover:border hover:border-black"
      >
        Download
      </button>
    </div>
  );
};

export default ManagedLotCard;