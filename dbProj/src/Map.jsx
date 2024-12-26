/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
'use client'

import React, { useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import 'leaflet-routing-machine';

// Custom marker icon
const customIcon = new L.Icon({
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

// Routing control component
function RoutingMachine({ start, end }) {
  const map = useMap();

  useEffect(() => {
    if (!map || !start || !end) return;

    const routingControl = L.Routing.control({
      waypoints: [L.latLng(start.lat, start.lng), L.latLng(end.lat, end.lng)],
      routeWhileDragging: true,
      lineOptions: {
        styles: [{ color: 'black', weight: 4 }],
      },
      show: false,
      addWaypoints: false,
      fitSelectedRoutes: true,
      showAlternatives: false,
    }).addTo(map);

    return () => map.removeControl(routingControl);
  }, [map, start, end]);

  return null;
}

const MapWithUserLocation = () => {
  const [userLocation, setUserLocation] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setUserLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude,
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

  if (loading) {
    return <div>Loading...</div>;
  }

  const position = userLocation ? [userLocation.lat, userLocation.lng] : [35, 35];
  const destination = { lat: 31.2, lng: 30 };

  return (
    <div style={{ height: '91vh', width: '50%' ,overflow:'hidden',borderRadius:'20px'}}> 
      <MapContainer
        center={position}
        zoom={13}
        style={{ height: '100%', width: '100%' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        {userLocation && (
          <Marker position={position} icon={customIcon}>
            <Popup>
              Your Location: {userLocation.lat.toFixed(4)}, {userLocation.lng.toFixed(4)}
            </Popup>
          </Marker>
        )}
        <Marker position={[destination.lat, destination.lng]} icon={customIcon}>
          <Popup>
            Destination: {destination.lat}, {destination.lng}
          </Popup>
        </Marker>
        {userLocation && <RoutingMachine start={userLocation} end={destination} />}
      </MapContainer>
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
    </div>
  );
};

export default MapWithUserLocation;

