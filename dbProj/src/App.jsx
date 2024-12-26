import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import MapWithUserLocation from './Map';
import MainPage from './Pages/MainPage';

function App() {
  return (
    <Router>
      <div className="w-full">
        <Routes>
          <Route path="/main" element={<MainPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
