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
        if (options.params && options.method === 'GET') {
          const queryParams = new URLSearchParams(options.params).toString();
          fetchUrl = `${url}?${queryParams}`;
        }

        const response = await fetch(fetchUrl, {
          method: options.method || 'GET',
          headers: {
            'Authorization': `Bearer ${options.token}`,
            'Content-Type': 'application/json',
            ...options.headers,
          },
          body: options.method !== 'GET' ? JSON.stringify(options.data) : null,
        });

        if (!response.ok) {
          throw new Error(`Error: ${response.statusText}`);
        }

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
