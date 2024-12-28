/* eslint-disable no-unused-vars */
import { useRecoilState } from 'recoil';
import { currPanel, changedLots } from '../state';
import React, { useEffect, useState } from 'react';
import { currUser } from '../state';
import ManagedLotCard from './ManagedLotCard';
import useFetch2 from '../Hooks/useFetch2';

const Insights = () => {
  const [panel, setPanel] = useRecoilState(currPanel);
  const [managedLots, setManagedLots] = useState([]);
  const token = localStorage.getItem('token');
  const [user, setUser] = useRecoilState(currUser);
  const [change, setChange] = useRecoilState(changedLots);

  const { data: destinations, loading: loading1, error: error1 } = useFetch2(
    'http://localhost:8080/lots/getmanagerlots',
    {
      method: 'GET',
      token: token,
      params: { managerId: user ? user.userId : ""},
    },
    [user.userId, change] // Dependency array to refetch when user or change updates
  );
  useEffect(() => {
    if (destinations) {
      // Assuming destinations is an array of lots
      const lotData = destinations.map((lot) => ({
        lotId: lot.lotId,
        name: lot.name,
      }));
      setManagedLots(lotData);
    }
  }, [destinations]);

  return (
    <div
      className={`${
        panel === 3 ? 'w-full lg:w-[40%] p-5' : 'w-0'
      } bg-white text-black overflow-hidden ease-in-out duration-300 transition-all`}
    >
      <h1 className="text-3xl text-black mb-4">Insights</h1>
      {loading1 && <p>Loading...</p>}
      {error1 && <p>Error: {error1.message}</p>}
      {managedLots.map((managedLot, index) => (
        <ManagedLotCard key={index} lotId={managedLot.lotId} lotName={managedLot.name} />
      ))}
    </div>
  );
};

export default Insights;
