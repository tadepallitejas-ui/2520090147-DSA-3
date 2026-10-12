# Lyrics Similarity & Attribution System

Full-stack version of the DSA-3 course project.

**Stack:** React (frontend) + Spring Boot (backend) + MongoDB (database)

## How it works

1. Songs are stored in MongoDB with precomputed **shingles** (3-word n-grams) — a
   HashSet-based structure so similarity lookups don't re-tokenize lyrics every time.
2. A submitted query lyric is shingled the same way, then compared against every
   stored song using **Jaccard similarity** (`|A ∩ B| / |A ∪ B|`).
3. A min-heap (`PriorityQueue`) keeps the top 5 matches in O(n log k) instead of
   sorting the whole corpus.

This keeps the core DSA techniques from the original abstract (HashMaps/Sets,
string comparison, ranking) — just running behind a real API and UI instead of
an in-memory array.

## Backend (Spring Boot)

Requires Java 17+ and Maven. MongoDB must be running locally (or update the URI
in `application.properties` to point at MongoDB Atlas).

```bash
cd backend
mvn spring-boot:run
```

Runs on `http://localhost:8080`.

### Endpoints

| Method | Path          | Description                          |
|--------|---------------|---------------------------------------|
| GET    | /api/songs    | List all stored songs                 |
| POST   | /api/songs    | Add a song `{ title, artist, lyrics }`|
| DELETE | /api/songs/{id} | Remove a song                       |
| POST   | /api/query    | Submit `{ lyric }`, get ranked matches |

## Frontend (React + Vite)

Requires Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

Runs on `http://localhost:5173`.

## Next steps / ideas to extend

- Seed the database with a starter set of songs (script or a `/api/songs` loop)
- Add an "Add Song" form in the UI so the corpus can grow without hitting the API directly
- Swap Jaccard similarity for cosine similarity on TF-IDF vectors if you want to show a comparison in your report
- Add pagination once the corpus gets large
