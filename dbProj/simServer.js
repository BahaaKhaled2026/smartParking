import fetch from 'node-fetch';

const user = {
  id: 10,
  username: 'sim',
  password: 'Simserver@1234',
  email: 'sim@server.com',
};

const BASE_URL = 'http://localhost:8080';
let activeReservations = [];
let token = 'server';



const constructQueryParams = (params) =>
  Object.entries(params)
    .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
    .join('&');

const fetchLots = async () => {
  try {
    const queryParams = constructQueryParams({});
    const url = `${BASE_URL}/lots/getlots?${queryParams}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error('Failed to fetch lots');
    return await response.json();
  } catch (error) {
    console.error('Error fetching lots:', error);
    return [];
  }
};

const fetchSpots = async (lotId) => {
  try {
    const queryParams = constructQueryParams({ lotId });
    const url = `${BASE_URL}/spots/getspots?${queryParams}`;
    console.log(url);
    
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error('Failed to fetch spots');
    const spots = await response.json();
    
    return spots.filter((spot) => spot.status === 'AVAILABLE'); 
  } catch (error) {
    console.error(`Error fetching spots for lot ${lotId}:`, error);
    return [];
  }
};

const reserveSpot = async (spot) => {
  const obj = {
    userId: user.id,
    spotId: spot.spotId,
    startTime : new Date(Date.now() + 60 * 60 * 1000*5).toISOString(),
    endTime: new Date(Date.now() + 60 * 60 * 1000*7).toISOString(),
    spotType: spot.type,
    status: 'ACTIVE',
    cost: 0,
    penalty: 0,
    spotNumber: spot.spotNumber,
    lotId:spot.lotId,
    lotName:"serverRes"
  };
  console.log(obj);
  
  console.log(`${BASE_URL}/reservations/reserve?reserve=true`);
  

  try {
    const response = await fetch(`${BASE_URL}/reservations/reserve?reserve=true`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(obj),
    });
    
    if (!response.ok) throw new Error('Failed to reserve spot');
    const reservation = await response.json();
    
    activeReservations.push(reservation.reservationId);
    console.log(`Reserved spot ${spot.spotId} successfully!`);
  } catch (error) {
    console.error('Error reserving spot:', error);
  }
};

const cancelReservation = async (reservationId) => {
  try {
    const url = `${BASE_URL}/reservations/cancel?reservationId=${reservationId}`;
    console.log(url);

    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error('Failed to cancel reservation');
    activeReservations = activeReservations.filter((id) => id !== reservationId);
    console.log(`Cancelled reservation ${reservationId} successfully!`);
  } catch (error) {
    console.error('Error cancelling reservation:', error);
  }
};

const main = async () => {

 
  setInterval(async () => {
    const lots = await fetchLots();
    
    if (lots.length === 0) return;
    
    const randomLot = lots[Math.floor(Math.random() * lots.length)];
    const spots = await fetchSpots(randomLot.lotId);
    if (spots.length === 0) return;
    
    const randomSpot = spots[Math.floor(Math.random() * spots.length)];
    
    await reserveSpot(randomSpot);
  }, Math.random() * 5000 + 2000); // Random interval between 5-10 seconds

  // Periodically cancel reservations
  setInterval(async () => {
    if (activeReservations.length === 0) return;
    console.log(activeReservations);
    
    const randomReservation = activeReservations[Math.floor(Math.random() * activeReservations.length)];
    await cancelReservation(randomReservation);
  }, Math.random() * 7000 + 7000); // Random interval between 7-14 seconds
};

main();
