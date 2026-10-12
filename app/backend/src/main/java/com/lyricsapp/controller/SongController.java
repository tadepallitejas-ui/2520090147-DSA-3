package com.lyricsapp.controller;

import com.lyricsapp.model.Song;
import com.lyricsapp.repository.SongRepository;
import com.lyricsapp.service.SimilarityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongRepository songRepository;
    private final SimilarityService similarityService;

    public SongController(SongRepository songRepository, SimilarityService similarityService) {
        this.songRepository = songRepository;
        this.similarityService = similarityService;
    }

    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Song> addSong(@Valid @RequestBody Song song) {
        // Precompute shingles once at write time so query time stays fast.
        song.setShingles(similarityService.shingle(song.getLyrics()));
        Song saved = songRepository.save(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        songRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
