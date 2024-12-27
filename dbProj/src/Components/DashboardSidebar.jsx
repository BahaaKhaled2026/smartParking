/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from "react";
import { currPanel } from "../state";
import { useRecoilState } from "recoil";

export function DashboardSidebar({ className, handleLogout }) {
  const [panel, setPanel] = useRecoilState(currPanel);
  
  return (
    <div
      className={`bg-black flex flex-col items-center md:items-start text-white pb-12 min-h-screen ${className} w-20 md:w-60`} // Set width to w-20 on smaller screens and w-60 on medium and up
    >
      <div className="space-y-4 py-4">
        <div className="px-4 py-2">
          <h2 className="mb-2 px-2 text-2xl font-semibold font-mono tracking-tight text-primary hidden md:block">
            RAKENY
          </h2>
        </div>
        <nav className="space-y-1">
          {/* Dashboard Button */}
          <button 
            onClick={() => setPanel(1)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left"
          >
            <i className="fa-solid fa-bars"></i>
            <span className="hidden md:block">Dashboard</span> 
          </button>

          {/* Book Button */}
          <button 
            onClick={() => setPanel(2)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-plus"></i>
            <span className="hidden md:block">Book</span>
          </button>

          {/* Insights Button */}
          <button 
            onClick={() => setPanel(3)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-chart-line"></i>
            <span className="hidden md:block">Insights</span>
          </button>

          {/* Profile Button */}
          <button 
            onClick={() => setPanel(4)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-user"></i>
            <span className="hidden md:block">Profile</span>
          </button>

          {/* Payment Details Button */}
          <button 
            onClick={() => setPanel(5)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-coins"></i>
            <span className="hidden md:block">Payment Details</span>
          </button>

          {/* Notifications */}
          <button 
            onClick={() => setPanel(6)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-envelope"></i>
            <span className="hidden md:block">Notifications</span>
          </button>

          {/* Logout Button */}
          <button 
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left"
            onClick={handleLogout}
          >
            <i className="fa-solid fa-sign-out"></i>
            <span className="hidden md:block">Logout</span>
          </button>
        </nav>
      </div>
    </div>
  );
}