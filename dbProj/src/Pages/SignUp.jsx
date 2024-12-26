import Header from "../Components/Header";
import SignupForm from "../Components/SignupForm";

const SignUp = () => {
    return ( 
        <div className="bg-white-100 w-full absolute top-0">
            <div className="header">
                <Header/>
            </div>
            <div className="flex-grow flex items-center justify-center w-full">
                <SignupForm/>
            </div>
        </div>
     );
}
 
export default SignUp;
