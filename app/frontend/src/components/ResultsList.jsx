export default function ResultsList({ results }) {
  if (!results) return null;

  if (results.length === 0) {
    return <p className="empty-state">No matches found.</p>;
  }

  return (
    <table className="results-table">
      <thead>
        <tr>
          <th>#</th>
          <th>Title</th>
          <th>Artist</th>
          <th>Similarity</th>
        </tr>
      </thead>
      <tbody>
        {results.map((r, i) => (
          <tr key={r.songId}>
            <td>{i + 1}</td>
            <td>{r.title}</td>
            <td>{r.artist}</td>
            <td>{(r.similarityScore * 100).toFixed(1)}%</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
