/* eslint-disable no-unused-vars */
import { useRecoilState } from 'recoil';
import { currPanel, changedLots } from '../state';
import { useState } from 'react';
import { currUser } from '../state';
import ManagedLotCard from './ManagedLotCard';

const Insights = () => {
  const [panel, setPanel] = useRecoilState(currPanel);
  const [managedLots, setManagedLots] = useState([
    { name: "Lot 1", lotId: 1 },
    { name: "Lot 2", lotId: 2 },
    { name: "Lot 3", lotId: 3 }
  ]);
  const token = localStorage.getItem('token');
  const [user, setUser] = useRecoilState(currUser);
  const [change, setChange] = useRecoilState(changedLots);
  // Log managedLots for debugging
  console.log(managedLots);
  console.log(managedLots[1]?.lotId);
  console.log(managedLots[1]?.name);


  
  return (
    <div className={`${panel === 3 ? 'w-full lg:w-[40%] p-5' : 'w-0'} bg-white text-black overflow-hidden ease-in-out duration-300 transition-all`}>
      <h1 className="text-3xl text-black mb-4">Insights</h1>
      {managedLots.map((managedLot, index) => (
        <ManagedLotCard key={index} lotId={managedLot.lotId} lotName={managedLot.name} />
      ))}
    </div>
  );
};

export default Insights;