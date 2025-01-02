import { useRef, useState } from "react";
import { useRecoilState } from "recoil";
import { currPanel, currUser } from "../state";
import { addLotUtil } from "../parkUtils/parkUtils";

const AddLot = () => {
    const latRef=useRef();
    const longRef=useRef();
    const nameRef=useRef();
    const capRef=useRef();
    const [panel,setPanel]=useRecoilState(currPanel);
    const [user,setUser]=useRecoilState(currUser);
    const token =localStorage.getItem('token');
    const [result,setResult]=useState("");
    async function addLot(){
        if((latRef.current.value=="" || longRef.current.value=="" || nameRef.current.value=="" || capRef.current.value=="")){
            return;
        }
        let lotObj={};
        lotObj.latitude=latRef.current.value;
        lotObj.longitude=longRef.current.value;
        lotObj.name=nameRef.current.value;
        lotObj.capacity=Number(capRef.current.value);
        lotObj.totalRevenue=0;
        lotObj.totalPenalty=0;
        lotObj.managerId=user.userId;
        console.log(lotObj);
        let res=await addLotUtil(lotObj,token);
        console.log(res);
        
    }
    return ( 
        <div className={`${panel === 9 ? 'w-full lg:w-[40%] p-5 max-h-[calc(100vh)] overflow-y-auto' : 'w-0'} bg-white absolute flex flex-col gap-4 text-black overflow-hidden  ease-in-out duration-300 transition-all`}>
            <h1 className="text-black text-3xl">Add Lot to Your System</h1>
            <p>Lot Name</p>
            <input className="rounded-lg border-2" type="text" ref={nameRef}/>
            <p>Lot Capacity</p>
            <input className="rounded-lg border-2" type="number" ref={capRef}/>
            <div className="">
                <p>Latitude</p>
                <input className="rounded-lg border-2" ref={latRef} type="text" name="" id="" />
            </div>
            <div className="">
                <p>Longitude</p>
                <input className="rounded-lg border-2" ref={longRef} type="text" name="" id="" />
            </div>
            <button className="bg-black p-3 text-white hover:bg-slate-400 transition-all ease-in-out duration-200" onClick={addLot}>Add Lot</button>
        </div>
     );
}
 
export default AddLot;