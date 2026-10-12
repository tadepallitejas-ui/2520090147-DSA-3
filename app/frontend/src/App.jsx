import { useState, useEffect, useCallback } from 'react';
import QueryForm from './components/QueryForm';
import ResultsList from './components/ResultsList';
import AddSongForm from './components/AddSongForm';
import SongList from './components/SongList';
import { getAllSongs } from './api';

export default function App() {
  const [results, setResults] = useState(null);
  const [songs, setSongs] = useState([]);
  const [loadError, setLoadError] = useState(null);

  const refreshSongs = useCallback(async () => {
    try {
      const data = await getAllSongs();
      setSongs(data);
      setLoadError(null);
    } catch (err) {
      setLoadError(err.message);
    }
  }, []);

  useEffect(() => {
    refreshSongs();
  }, [refreshSongs]);

  return (
    <div className="app">
      <h1>Lyrics Similarity &amp; Attribution System</h1>
      <p className="subtitle">Paste a lyric snippet to find the closest matching songs in the corpus.</p>

      <QueryForm onResults={setResults} />
      <ResultsList results={results} />

      <hr />

      <AddSongForm onAdded={refreshSongs} />

      <hr />

      <section>
        <h2>Corpus ({songs.length} songs)</h2>
        {loadError && <p className="error">{loadError}</p>}
        <SongList songs={songs} />
      </section>
    </div>
  );
}
