/* eslint-disable no-unused-vars */
import { StrictMode } from 'react'
import { WebSocketProvider } from './Components/WebSocketProvider.jsx'

import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { RecoilRoot } from 'recoil';

createRoot(document.getElementById('root')).render(
    <RecoilRoot>
         <App />
    </RecoilRoot>,
)
