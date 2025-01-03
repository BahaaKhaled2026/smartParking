/* eslint-disable no-unused-vars */
import { Suspense, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import useAuth from './hooks/useAuth';
import MapWithUserLocation from './Map'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { DashboardSidebar } from './Components/DashboardSidebar'
import Intro from './Pages/IntroPage';
import Login from './Pages/Login';
import SignUp from './Pages/SignUp';
import Dashboard from './Pages/Dashboard';
import ResetPasswordPage from './Pages/ResetPasswordPage';
import MainPage from './Pages/MainPage';
import { WebSocketProvider } from './Components/WebSocketProvider.jsx'


function App() {
  useAuth();

  return (
    <div  className="flex w-full justify-between items-center">
      <WebSocketProvider>
        <Router>
          <Suspense>
            <Routes>
              {/* <Route path="/" element={<Intro />} /> */}
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/resetpassword" element={<ResetPasswordPage />} />
              <Route path="/" element={<MainPage />} />
            </Routes>
          </Suspense>
        </Router>
      </WebSocketProvider>


      
      {/* <DashboardSidebar/>
      <MapWithUserLocation/> */}
    </div>
  )
}

export default App;
