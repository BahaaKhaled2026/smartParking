import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import MapWithUserLocation from './Map'
import { DashboardSidebar } from './Components/DashboardSidebar'

function App() {
  return (
    <div  className="flex w-full justify-between items-center">
      <DashboardSidebar/>
      <MapWithUserLocation/>
    </div>
  )
}

export default App
