import { useState } from 'react';
import { submitQuery } from '../api';

export default function QueryForm({ onResults }) {
  const [lyric, setLyric] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!lyric.trim()) return;

    setLoading(true);
    setError(null);
    try {
      const results = await submitQuery(lyric);
      onResults(results);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="query-form">
      <textarea
        value={lyric}
        onChange={(e) => setLyric(e.target.value)}
        placeholder="Paste a lyric snippet to check..."
        rows={5}
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Searching...' : 'Find Matches'}
      </button>
      {error && <p className="error">{error}</p>}
    </form>
  );
}
