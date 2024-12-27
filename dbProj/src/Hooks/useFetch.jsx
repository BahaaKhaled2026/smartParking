import { useState, useEffect } from 'react';

const useFetch = (url, options = {}, dependencies = []) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // Construct URL with query parameters if provided
        let fetchUrl = url;
        if (options.params) {
          const queryParams = new URLSearchParams(options.params).toString();
          fetchUrl = `${url}?${queryParams}`;
        }

        // Prepare fetch options
        const fetchOptions = {
          method: options.method || 'GET',
          headers: {
            'Authorization': `Bearer ${options.token || ''}`,
            'Content-Type': 'application/json',
            ...options.headers,
          },
        };

        // Add body only if it's not a GET request
        if (options.method && options.method !== 'GET' && options.data) {
          fetchOptions.body = JSON.stringify(options.data);
          console.log(options.data);
          
        }

        // Perform the fetch request
        const response = await fetch(fetchUrl, fetchOptions);

        if (!response.ok) {
          throw new Error(`Error: ${response.statusText}`);
        }

        // Parse response
        const result = await response.json();
        setData(result);
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, dependencies);

  return { data, loading, error };
};

export default useFetch;
