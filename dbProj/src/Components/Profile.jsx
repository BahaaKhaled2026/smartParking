import React from 'react';
import { useRecoilState } from "recoil";
import { currPanel, currUser } from "../state";

const Profile = () => {
    const [user, setUser] = useRecoilState(currUser);
    const [panel,setPanel]=useRecoilState(currPanel);
    return ( 
        <div className={`${panel === 4 ? 'w-full lg:w-[40%] opacity-100 p-2 max-h-[calc(100vh)] overflow-y-auto' : 'opacity-0 w-0 overflow-hidden'} transition-all ease-in-out duration-300 absolute`}>
            <div className="text-center p-6 bg-gray-800 border-b">
                <h1 className="text-4xl font-bold text-white">{user.username}</h1>
                <p className="text-sm text-gray-300 mt-1">{user.role}</p>
            </div>
            <div className="p-6">
                <div className="grid grid-cols-1 gap-6 mb-6">
                    <div>
                        <p className="text-gray-700 font-bold mb-2">Email</p>
                        <p className="text-gray-600">{user.email}</p>
                    </div>
                    <div>
                        <p className="text-gray-700 font-bold mb-2">License Plate</p>
                        <p className="text-gray-600">{user.licensePlate}</p>
                    </div>
                    <div>
                        <p className="text-gray-700 font-bold mb-2">Balance</p>
                        <p className="text-gray-600">${user.balance.toFixed(2)}</p>
                    </div>
                    <div>
                        <p className="text-gray-700 font-bold mb-2">Total Penalty</p>
                        <p className="text-gray-600">${user.totalPenalty.toFixed(2)}</p>
                    </div>
                    <div>
                        <p className="text-gray-700 font-bold mb-2">User ID</p>
                        <p className="text-gray-600">{user.userId}</p>
                    </div>
                    <div>
                        <p className="text-gray-700 font-bold mb-2">Account Created</p>
                        <p className="text-gray-600">{new Date(user.createdAt).toLocaleDateString()}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
 
export default Profile;
