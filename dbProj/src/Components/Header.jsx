/* eslint-disable no-unused-vars */
import { useRecoilState } from 'recoil';
import Logo from '../assets/logo.jpg';
import { useNavigate } from 'react-router-dom';
import { userState } from '../recoil/atoms';

function Header() {
  const navigate = useNavigate();
  const [user,setUser]=useRecoilState(userState);

  return (
    <div className="p-3 flex justify-between items-center px-4 sm:px-12">
      <img src={Logo} alt="Smart City Parking Logo" style={{ width: '150px', height: 'auto', borderRadius: '60px'}}/>
      <div className="flex items-center">
        <button className="text-black bg-white px-4 py-2 rounded-lg mr-4 text-lg font-semibold hover: bg-gray-200 hover:text-gray-800" onClick={()=>navigate('/login')}>Login</button>
        <button className="text-black bg-white px-4 py-2 rounded-lg text-lg font-semibold hover: bg-gray-200 hover:text-gray-800" onClick={()=>navigate('/signup')}>Sign Up</button>
        </div>
    </div>
    );
}

export default Header;
