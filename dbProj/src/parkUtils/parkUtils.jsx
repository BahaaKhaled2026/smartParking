export async function reserveSpot(token, reservation, reserve) {
    const endpoint = `http://localhost:8080/reservations/reserve?reserve=${reserve}`;
  
    try {
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`, // Include the token for authentication
        },
        body: JSON.stringify(reservation), // Serialize the reservation object
      });
  
      // Check for non-2xx responses
      if (!response.ok) {
        const contentType = response.headers.get('Content-Type');
        if (contentType && contentType.includes('application/json')) {
          const errorData = await response.json();
          return {NotValid:true,errorMsg:errorData.error}
          throw new Error(errorData.error || 'Unknown error occurred');
        } else {
          const errorText = await response.text();
          throw new Error(errorText);
        }
      }
  
      // Successfully parse the JSON response
      const data = await response.json();
      console.log('Successful response:', data); // Log the successful response
      return data;
    } catch (error) {
      console.error('Error:', error.message);
      throw error;
    }
}
export const cancelReservation = async (reservationId,token) => {
  try {
    const url = `http://localhost:8080/reservations/cancel?reservationId=${reservationId}`;
    console.log(url);

    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error('Failed to cancel reservation');
    console.log(`Cancelled reservation ${reservationId} successfully!`);
  } catch (error) {
    window.alert(error)
    console.error('Error cancelling reservation:', error);
  }
};
export async function getReservation(token, uID) {
    const endpoint = `http://localhost:8080/reservations/user?userId=${uID}`;
  
    try {
      const response = await fetch(endpoint, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });
      if (!response.ok) {
        const contentType = response.headers.get('Content-Type');
        if (contentType && contentType.includes('application/json')) {
          const errorData = await response.json();
          throw new Error(errorData.error || 'Unknown error occurred');
        } else {
          const errorText = await response.text();
          throw new Error(errorText);
        }
      }
      const data = await response.json();
      console.log('Successful response:', data);
      return data;
    } catch (error) {
      console.error('Error:', error.message);
      throw error;
    }
}
export const arriveReservation = async (reservationId,token) => {
  try {
    const url = `http://localhost:8080/reservations/arrive?reservationId=${reservationId}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    
    if (!response.ok) throw new Error('Failed to arrive reservation');
    console.log(`Cancelled reservation ${reservationId} successfully!`);
  } catch (error) {
    window.alert(error)
    console.error('Error cancelling reservation:', error);
  }
};
  