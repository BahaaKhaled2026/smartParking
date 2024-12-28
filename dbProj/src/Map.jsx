/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import 'leaflet-routing-machine';
import { useRecoilState } from 'recoil';
import { chosenLot, currUser } from './state';
import useFetch from './Hooks/useFetch';
import useFetch2 from './Hooks/useFetch2';

const defaultIcon = new L.Icon({
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

const blackIcon = new L.Icon({
  iconUrl: 'https://static.vecteezy.com/system/resources/previews/022/187/623/large_2x/map-location-pin-icon-free-png.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

function RoutingMachine({ start, end }) {
  const [lot,setLot]=useRecoilState(chosenLot);

  const map = useMap();

  useEffect(() => {
    if (!map || !start || !end) return;

    const routingControl = L.Routing.control({
      waypoints: [L.latLng(start.lat, start.lng), L.latLng(end.lat, end.lng)],
      routeWhileDragging: false,
      lineOptions: {
        styles: [{ color: 'black', weight: 4 }],
      },
      createMarker: () => null,
      addWaypoints: false,
      fitSelectedRoutes: true,
      showAlternatives: false,
    }).addTo(map);

    return () => map.removeControl(routingControl);
  }, [map, start, end,lot]);

  return null;
}
const MapWithUserLocation = ({mobile}) => {
  const [userLocation, setUserLocation] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedDestinationId, setSelectedDestinationId] = useState(null);
  const [lot,setLot]=useRecoilState(chosenLot);
  const token=localStorage.getItem('token');
  const[user,setUser]=useRecoilState(currUser);
  const { data: destinations, loading: loading1, error: error1 } = useFetch2('http://localhost:8080/lots/getlots', {
    method: 'GET',
    token: token,
    params: {}
  }, []);
  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setUserLocation({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
          });
          setLoading(false);
        },
        (error) => {
          setError(error.message);
          setLoading(false);
        }
      );
    } else {
      setError('Geolocation is not supported by this browser.');
      setLoading(false);
    }
  }, []);

  if (!destinations) {
    return <div>Loading...</div>;
  }

  const position = userLocation ? [userLocation.latitude, userLocation.longitude] : [35, 35];

  

  const handleMarkerClick = (destination) => {
    setLot(destination);
    setSelectedDestinationId(destination.lotId);
  };

  return (
    <div  style={{ height: mobile?'300px':'100vh', width: mobile?'100%':'100%', overflow: 'hidden', borderRadius: '20px' }}>
      <MapContainer center={position} zoom={13} style={{ height: '100%', width: '100%' }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        {userLocation && (
          <Marker position={position} icon={blackIcon}>
            <Popup>
              Your Location: {userLocation.latitude.toFixed(4)}, {userLocation.longitude.toFixed(4)}
            </Popup>
          </Marker>
        )}
        {destinations.map((destination) => (
          <Marker
            key={destination.lotId}
            position={[destination.latitude, destination.longitude]}
            icon={selectedDestinationId === destination.lotId ? blackIcon : defaultIcon}
            eventHandlers={{
              click: () => handleMarkerClick(destination),
            }}
          >
            <Popup>{destination.name}</Popup>
          </Marker>
        ))}
        {userLocation && selectedDestinationId && (
          <RoutingMachine
            start={{ lat: userLocation.latitude, lng: userLocation.longitude }}
            end={{
              lat: destinations.find((d) => d.lotId === selectedDestinationId)?.latitude,
              lng: destinations.find((d) => d.lotId === selectedDestinationId)?.longitude,
            }}
          />
        )}
      </MapContainer>
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
    </div>
  );
};

export default MapWithUserLocation;
