import { useState } from 'react';
import { addSong } from '../api';

export default function AddSongForm({ onAdded }) {
  const [title, setTitle] = useState('');
  const [artist, setArtist] = useState('');
  const [lyrics, setLyrics] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);

    if (!title.trim() || !artist.trim() || !lyrics.trim()) {
      setError('All fields are required.');
      return;
    }

    setLoading(true);
    try {
      const saved = await addSong({ title, artist, lyrics });
      setTitle('');
      setArtist('');
      setLyrics('');
      setSuccess(true);
      if (onAdded) onAdded(saved);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="add-song-form">
      <h2>Add a Song</h2>
      <input
        type="text"
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <input
        type="text"
        placeholder="Artist"
        value={artist}
        onChange={(e) => setArtist(e.target.value)}
      />
      <textarea
        placeholder="Lyrics"
        rows={4}
        value={lyrics}
        onChange={(e) => setLyrics(e.target.value)}
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Adding...' : 'Add Song'}
      </button>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">Song added.</p>}
    </form>
  );
}
