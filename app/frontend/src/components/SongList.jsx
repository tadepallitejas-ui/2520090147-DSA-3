export default function SongList({ songs }) {
  if (!songs || songs.length === 0) {
    return <p className="empty-state">No songs in the corpus yet.</p>;
  }

  return (
    <ul className="song-list">
      {songs.map((s) => (
        <li key={s.id}>
          <strong>{s.title}</strong> — {s.artist}
        </li>
      ))}
    </ul>
  );
}
