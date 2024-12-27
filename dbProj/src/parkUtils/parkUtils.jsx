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
  