/* eslint-disable no-unused-vars */
import { useRef, useState } from "react";
import { useNavigate } from 'react-router-dom';
import useFetch from "../hooks/useFetch";
import { authUsingProv, sendOtp, validateForm } from "../Utils/formUtils";
import { isAuthenticatedState, userState } from "../recoil/atoms";
import { useRecoilState } from "recoil";
import SignUpHeader from "./SignupHeader";
import ContinueSep from "./ContinueSep";
import { toast } from "react-toastify";

const SignupForm = () => {
    const {error,post}=useFetch();
    const [otpDone,setOtpDone]=useState(false);
    const [showOtp,setShowOpt]=useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loginError, setLoginError] = useState('');
    const [otp,setOpt]=useState(0);
    const [errors, setErrors] = useState({});
    const [user, setUser] = useRecoilState(userState);
    const [isAuthenticated, setIsAuthenticated] = useRecoilState(isAuthenticatedState);

    let otpRef=useRef(null);

    const resetLoginForm = () => {
        setEmail('');
        setPassword('');
        setLoginError('');
      };

    const [formData,setFormData]=useState({
        email:"",
        password:"",
        username:"",
        firstName:"",
        lastName:"",
        gender:"MALE",
        role:"DRIVER",
        dateOfBirth:"",
        licensePlate:"",
    })
    const navigate=useNavigate();

    function handleChange(e){
        setFormData({...formData,[e.target.name]:e.target.value});
    }

    const generateOtp = () => {
        return Math.floor(100000 + Math.random() * 900000);
    };

    

    function signUpProv(userObj,provIndex){
        if(!userObj || !userObj.email){
            return
        }
        let formatted=
        {
            email:userObj.email,
            password:"",
            username:"",
            firstName:userObj.firstName,
            lastName:"",
            gender:"NOT_SPECIFIED",
            userType:"USER",
            dateOfBirth:"",
            licensePlate:"",
        }
    }

    function verify(){
        let temp=generateOtp();
        console.log(temp);
        
        setOpt(temp);
        sendOtp(temp,formData.email);
    }

    function checkOtp(){
        setOtpDone(otp==otpRef.current.value);
    }

    function handleSubmit(){
        console.log(formData);
        let res=validateForm(formData);        
        if(!res.valid){
            setErrors(res.errors);
            return;
        }
        if(otpDone){
            if(res.valid){
                post("http://localhost:8080/users/signup",
                    formData,
                    (res)=> {
                        if(!res){
                            return;
                        }
                        console.log(res);
                        localStorage.setItem("token",res.token);
                        localStorage.setItem("user",JSON.stringify(res.user));
                        setUser(res.user);
                        setIsAuthenticated(true);
                        toast.success("Sign up successful");
                        console.log("Sign up successful");
                        navigate("/dashboard");
                    },
                    ()=>{
                        window.scrollTo({
                            top: 0,
                            behavior: 'smooth'
                        });
                });
            }
            else {
                setErrors(res.errors);
            }
        }
        else {
            setShowOpt(true);
            setErrors({});
            verify();
        }
    }

    return ( 
        <div className="w-[90%]  md:w-[50%] lg:w-[30%] bg-white rounded-lg shadow-2xl p-8 font-Roboto ">
            <SignUpHeader/>
            {error&&
            <div>
                <p className="text-red-600">
                    {error}
                </p>
            </div>
            }
            <div className="flex flex-col gap-5 w-full">
                <div className="flex items-center justify-between">
                    <div>
                        <h3 className="emailLabel mb-2">
                            First Name
                        </h3>
                        <input
                            placeholder="Enter first name"
                            type = "text"
                            onChange = { handleChange }
                            value = { formData.firstName }
                            name = "firstName"
                            className = { `email-input text-gray-500 w-full border-2 rounded-lg h-8 text-sm p-2 ${ errors.firstName ? "border-red-500" : "" }` }
                        />
                    </div>
                    <div>
                        <h3 className = "emailLabel mb-2">
                            Last Name
                        </h3>
                        <input 
                            placeholder = "Enter last name"
                            type = "text"
                            onChange = { handleChange }
                            value = { formData.lastName }
                            name = "lastName"
                            className = { `email-input text-gray-500 w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.lastName ? "border-red-500" : ""}` }
                        />
                    </div>
                </div>
                <div className="w-full">
                    <h3 className="emailLabel mb-2">
                        Gender
                    </h3>
                    <select
                        className="email-input text-gray-400 w-full border-2 border-gray-300 rounded-lg"
                        onChange = { handleChange }
                        value={formData.gender}
                        name="gender"
                        id=""
                    >
                        <option value="MALE">
                            Male
                        </option>
                        <option value="FEMALE">
                            Female
                        </option>
                    </select>
                </div>
                <div className="w-full">
                    <h3 className="emailLabel mb-2">
                        Birth date
                    </h3>
                    <input
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        className={`email-input text-gray-400 border-2 border-gray-300 rounded-lg w-full ${errors.dateOfBirth ? "border-red-500" : ""}`}
                        type="date" 
                    />
                </div>
                <div className="">
                    <h3 className="emailLabel mb-2">
                        Username
                    </h3>
                    <input 
                        placeholder="Enter username" 
                        type="text" 
                        onChange = { handleChange } 
                        value = { formData.username } 
                        name = "username" 
                        className = { `email-input text-gray-400 w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.username ? "border-red-500" : ""}` } 
                    />
                </div>
                <div className="w-full">
                    <h3 className="emailLabel mb-2">
                        Role
                    </h3>
                    <select
                        className="email-input text-gray-400 w-full border-2 border-gray-300 rounded-lg"
                        onChange = { handleChange }
                        value={formData.role}
                        name="role"
                        id=""
                    >
                        <option value="DRIVER">
                            DRIVER
                        </option>
                        <option value="MANAGER">
                            MANAGER
                        </option>
                    </select>
                </div>
                <div className="w-full">
                    <h3 className="emailLabel mb-2">License Plate</h3>
                    <input
                    placeholder="Enter license plate"
                    type="text"
                    onChange={handleChange}
                    value={formData.licensePlate}           //AB12 CDE
                    name="licensePlate"
                    className={`email-input text-gray-400 w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.licensePlate ? "border-red-500" : ""}`}
                    />
                </div>
                <div className="">
                    <h3 className="emailLabel mb-2">
                        Email
                    </h3>
                    <input
                        placeholder = "Enter email"
                        type = "email"
                        onChange = { handleChange }
                        value = { formData.email }
                        name = "email"
                        className = { `email-input text-gray-400 w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.email ? "border-red-500" : ""}` }
                    />
                </div>
                <div className="">
                    <h3 className="emailLabel mb-2">
                        Password
                    </h3>
                    <input 
                        placeholder = "Enter password"  
                        type = "password" 
                        onChange = { handleChange } 
                        value = { formData.password }  
                        name = "password" 
                        className = { `email-input text-gray-400 w-full border-2 rounded-lg h-8 text-sm p-2 ${errors.password ? "border-red-500" : ""}` }
                    />
                </div>
                <div className = { `${showOtp?'max-h-[500px]':'max-h-0'} transition-all ease-in-out duration-1000 overflow-hidden` }>
                    <h3 className="emailLabel mb-2">
                        Email Otp
                    </h3>
                    <div className="flex justify-between">
                        <input 
                            ref={otpRef}
                            placeholder="Enter Otp sent to your email"
                            type="password"
                            className="email-input text-gray-400 w-[85%] border-2 rounded-lg h-6 text-sm p-2 "
                        />
                        <button 
                            className={`${otpDone?'bg-green-600 p-1 rounded-full':'bg-gray-300 ml-5'} h-7 w-7 shadow-xl flex justify-center items-center`}
                            onClick={checkOtp}>
                                <i className="fa-solid fa-check">
                                </i>
                        </button>
                    </div>
                </div>
                <button 
                    onClick = { handleSubmit } 
                    className="loginButton"
                >
                    Sign up
                </button>
                <ContinueSep/>
                {/* <div className="flex justify-between w-full">
                    <button onClick={async()=>{
                        let data=await authUsingProv(1);
                        signUpProv(data,1);
                    }} className="w-2/5 p-4 shadow-lg rounded-lg">
                        <i className="fa-brands fa-google"></i> Google
                    </button>
                    <button onClick={async()=>{
                        let data=await authUsingProv(0);
                        signUpProv(data,0);
                    }} className="w-2/5 p-4 shadow-lg rounded-lg">
                        <i className="fa-brands fa-facebook-f"></i> Facebook
                    </button>
                </div> */}
                <p className="text-center text-md text-gray-500  font-semibold">
                    Already have an account? 
                    <a href="/login" className='hyperlinks'>
                    Log In
                    </a>
                </p>
            </div>
        </div>
     );
}
 
export default SignupForm;
