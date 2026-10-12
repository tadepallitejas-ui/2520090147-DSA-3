package com.lyricsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResult {
    private String songId;
    private String title;
    private String artist;
    private double similarityScore; // 0.0 - 1.0 (Jaccard similarity)
}
