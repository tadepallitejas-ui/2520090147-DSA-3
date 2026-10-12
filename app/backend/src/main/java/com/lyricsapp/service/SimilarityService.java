package com.lyricsapp.service;

import com.lyricsapp.dto.MatchResult;
import com.lyricsapp.model.Song;
import com.lyricsapp.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SimilarityService {

    private static final Pattern NON_WORD = Pattern.compile("[^a-z0-9\\s]");
    private static final int SHINGLE_SIZE = 3; // 3-word shingles
    private static final int TOP_K = 5;

    private final SongRepository songRepository;

    public SimilarityService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Breaks lyrics into normalized word-shingles (n-grams).
     * Using a HashSet backed by a HashMap under the hood gives O(1) average
     * lookup/insert, which is what makes Jaccard similarity fast to compute
     * even as the corpus grows.
     */
    public Set<String> shingle(String text) {
        String cleaned = NON_WORD.matcher(text.toLowerCase()).replaceAll(" ").trim();
        String[] words = cleaned.split("\\s+");

        Set<String> shingles = new HashSet<>();
        if (words.length < SHINGLE_SIZE) {
            shingles.add(String.join(" ", words));
            return shingles;
        }

        for (int i = 0; i <= words.length - SHINGLE_SIZE; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < SHINGLE_SIZE; j++) {
                if (j > 0) sb.append(' ');
                sb.append(words[i + j]);
            }
            shingles.add(sb.toString());
        }
        return shingles;
    }

    /** Jaccard similarity = |A ∩ B| / |A ∪ B|, computed via HashSet operations. */
    public double jaccardSimilarity(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /**
     * Compares the query lyric against every stored song and returns the
     * top-K matches, ranked by similarity score (descending).
     */
    public List<MatchResult> findTopMatches(String queryLyric) {
        Set<String> queryShingles = shingle(queryLyric);
        List<Song> songs = songRepository.findAll();

        // Min-heap of size TOP_K keeps this O(n log k) instead of sorting
        // the whole corpus, which matters once the song collection grows.
        PriorityQueue<MatchResult> topMatches = new PriorityQueue<>(
                Comparator.comparingDouble(MatchResult::getSimilarityScore));

        for (Song song : songs) {
            Set<String> songShingles = song.getShingles() != null && !song.getShingles().isEmpty()
                    ? song.getShingles()
                    : shingle(song.getLyrics());

            double score = jaccardSimilarity(queryShingles, songShingles);
            MatchResult candidate = new MatchResult(song.getId(), song.getTitle(), song.getArtist(), score);

            if (topMatches.size() < TOP_K) {
                topMatches.offer(candidate);
            } else if (score > topMatches.peek().getSimilarityScore()) {
                topMatches.poll();
                topMatches.offer(candidate);
            }
        }

        return topMatches.stream()
                .sorted(Comparator.comparingDouble(MatchResult::getSimilarityScore).reversed())
                .collect(Collectors.toList());
    }
}
