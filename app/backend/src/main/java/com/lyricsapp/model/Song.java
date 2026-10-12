package com.lyricsapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "songs")
public class Song {

    @Id
    private String id;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "artist is required")
    private String artist;

    @NotBlank(message = "lyrics is required")
    private String lyrics;

    // Precomputed token shingles (word n-grams), built at insert time so
    // every query doesn't have to re-tokenize the whole corpus. This is
    // the HashMap/Set-backed piece carried over from the original DSA project.
    private Set<String> shingles;
}
