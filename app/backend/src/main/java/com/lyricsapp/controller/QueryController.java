package com.lyricsapp.controller;

import com.lyricsapp.dto.MatchResult;
import com.lyricsapp.dto.QueryRequest;
import com.lyricsapp.service.SimilarityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final SimilarityService similarityService;

    public QueryController(SimilarityService similarityService) {
        this.similarityService = similarityService;
    }

    @PostMapping
    public List<MatchResult> query(@Valid @RequestBody QueryRequest request) {
        return similarityService.findTopMatches(request.getLyric());
    }
}
