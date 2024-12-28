
import React, { useEffect, useState } from "react";
import { currPanel, currUser, notifi } from "../state";
import { useRecoilState, useRecoilValue } from "recoil";
import { notificationsState } from "../recoil/atoms";

export function DashboardSidebar({ className, handleLogout }) {
  const [panel, setPanel] = useRecoilState(currPanel);
  const [user, setUser] = useRecoilState(currUser);
  const notifications = useRecoilValue(notificationsState);
  const [not,setNot]=useRecoilState(notifi);
  
  useEffect(()=>{
    setNot(true);
  },[notifications])
  return (
    <div
      className={`bg-black flex flex-col items-center md:items-start text-white pb-12 min-h-[100%]  w-[30%] md:w-[18%]`} // Set width to w-20 on smaller screens and w-60 on medium and up
    >
      <div className="space-y-4 py-4">
        <div className="px-4 py-2">
          <h2 className="mb-2 px-2 text-2xl font-semibold font-mono tracking-tight text-primary hidden md:block">
            RAKENY
          </h2>
          <h2 className="text-2xl font-semibold font-mono tracking-tight text-primary block md:hidden">R</h2>
        </div>
        <nav className="space-y-1">
          { user && user.role === "DRIVER" ?(
              <>
               <button 
            onClick={() => setPanel(1)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left"
          >
            <i className="fa-solid fa-bars"></i>
            <span className="hidden md:block">Dashboard</span> 
          </button>

          <button 
            onClick={() => setPanel(2)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-plus"></i>
            <span className="hidden md:block">Book</span>
          </button>


          <button 
            onClick={() => setPanel(5)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-coins"></i>
            <span className="hidden md:block">Payment Details</span>
          </button>

              </>
            ): user && user.role === "MANAGER" ? (
              <>
                        <button 
            onClick={() => setPanel(3)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-chart-line"></i>
            <span className="hidden md:block">Insights</span>
          </button>
              </>
            ): 
            (
              <>
                             <button 
            onClick={() => setPanel(1)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left"
          >
            <i className="fa-solid fa-bars"></i>
            <span className="hidden md:block">Dashboard</span> 
          </button>

          <button 
            onClick={() => setPanel(2)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-plus"></i>
            <span className="hidden md:block">Book</span>
          </button>
          <button 
            onClick={() => setPanel(3)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-chart-line"></i>
            <span className="hidden md:block">Insights</span>
          </button>
              
              </>
            )
          }
          <button 
            onClick={() => setPanel(4)}
            className="w-full flex justify-start items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <i className="fa-solid fa-user"></i>
            <span className="hidden md:block">Profile</span>
          </button>

          <button 
            onClick={() => setPanel(6)}
            className=" w-full flex justify-between items-center gap-2 hover:bg-accent px-4 py-2 text-left">
            <div className="flex justify-center items-center gap-2">
              <i className="fa-solid fa-envelope"></i>
              <span className="hidden md:block">Notifications</span>
            </div>
            {not&&<i class="fa-solid fa-exclamation fa-beat" style={{color: "#ffffff"}}></i>}
          </button>

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