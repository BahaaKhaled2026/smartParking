/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from 'react';
import { useRecoilState } from 'recoil';
import { changedLots } from '../state';

const ManagedLotCard = ({ lotId, lotName }) => {
  const [change, setChange] = useRecoilState(changedLots);
  const token = localStorage.getItem('token');

  const handleDownload = async () => {
    try {
      const response = await fetch(`http://localhost:8080/users/manager/lots?lotId=${lotId}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
      }

      const blob = await response.blob(); // Convert the response to a binary object
      const url = window.URL.createObjectURL(blob); // Create a downloadable link
      const a = document.createElement('a');
      a.href = url;
      a.download = `Lot_${lotId}_Report.pdf`; // Set the default file name
      document.body.appendChild(a); // Append the link to the document
      a.click(); // Simulate a click to trigger the download
      a.remove(); // Remove the link after download
      window.URL.revokeObjectURL(url); // Clean up the URL object
      console.log("Report downloaded successfully.");
    } catch (error) {
      console.error("Error downloading report:", error.message);
      alert(`Failed to download report: ${error.message}`);
    }
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
