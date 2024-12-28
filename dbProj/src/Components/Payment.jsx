/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import { useRecoilState } from 'recoil';
import { currPanel, currUser } from '../state';

const Payment = () => {
  const [panel, setPanel] = useRecoilState(currPanel);
  const [price, setPrice] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [user, setUser] = useRecoilState(currUser);

  const token = localStorage.getItem('token'); // Retrieve the user's token

  const handlePriceChange = (e) => {
    const value = e.target.value;
    if (value === '' || /^[0-9]*\.?[0-9]*$/.test(value)) {
      setPrice(value);
      setError('');
      setSuccess('');
    } else {
      setError('Please enter a valid positive number');
      setSuccess('');
    }
  };

  const handlePayment = async () => {
    const parsedPrice = parseFloat(price);
    if (price === '' || isNaN(parsedPrice) || parsedPrice <= 0 || parsedPrice > 20000) {
      setError('Please enter a valid positive number between 0 and 20000');
      setSuccess('');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/users/addbalance?amount=${parsedPrice}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to add balance');
      }

      const res = await response.json(); // Read the success message
      setSuccess(res.message || `Payment of $${parsedPrice.toFixed(2)} made successfully`);
      setUser(res.user);
      localStorage.setItem('user',JSON.stringify(res.user));
      setError('');
      setPrice('');
    } catch (error) {
      console.error('Error making payment:', error.message);
      setError(`Failed to add payment: ${error.message}`);
      setSuccess('');
    }
  };

  return (
    <div
      className={`${
        panel === 5 ? 'w-full lg:w-[40%] p-5' : 'w-0'
      } bg-white-400 text-black overflow-hidden ease-in-out duration-300 transition-all`}
    >
      <h1 className="text-3xl text-black mb-4">Payment</h1>
      <input
        type="text"
        value={price}
        onChange={handlePriceChange}
        placeholder="Enter payment price"
        className="border p-2 mb-4 w-full"
      />
      {error && <p className="text-red-500">{error}</p>}
      {success && <p className="text-green-500">{success}</p>}
      <button
        onClick={handlePayment}
        className="bg-black text-white px-4 py-2 hover:bg-white hover:text-black hover:border hover:border-black"
      >
        Add Payment
      </button>
    </div>
  );
};

export default Payment;