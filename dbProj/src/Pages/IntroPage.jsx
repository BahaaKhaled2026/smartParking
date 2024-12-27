import Logo from "../assets/logo.jpg";
import { useNavigate } from 'react-router-dom';

function IntroPage() {
    const navigate = useNavigate();

    return(
        <div className="intro-container w-full h-full flex flex-col items-center justify-center">
            <img
                src={Logo}
                alt ="Smart City Parking Logo"
                style =
                {{ width: '600px', height: 'auto', borderRadius: '70px' }} 
            />
            <div>
                <button
                    className="signup-btn-intro"
                    onClick={() => navigate('/signup')}
                >
                    New to our Platform?
                </button>
                <br />
                <button
                    className="login-btn-intro"
                    onClick={() => navigate('/login')}
                >
                    Login            
                </button>
            </div>
        </div>
    )
}

export default IntroPage;
