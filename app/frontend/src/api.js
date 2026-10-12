const API_BASE = 'http://localhost:8080/api';

export async function submitQuery(lyric) {
  const res = await fetch(`${API_BASE}/query`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ lyric }),
  });
  if (!res.ok) {
    throw new Error(`Query failed: ${res.status}`);
  }
  return res.json();
}

export async function getAllSongs() {
  const res = await fetch(`${API_BASE}/songs`);
  if (!res.ok) {
    throw new Error(`Failed to load songs: ${res.status}`);
  }
  return res.json();
}

export async function addSong(song) {
  const res = await fetch(`${API_BASE}/songs`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(song),
  });
  if (!res.ok) {
    throw new Error(`Failed to add song: ${res.status}`);
  }
  return res.json();
}
