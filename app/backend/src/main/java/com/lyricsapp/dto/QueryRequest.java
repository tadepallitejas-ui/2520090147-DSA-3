package com.lyricsapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueryRequest {

    @NotBlank(message = "lyric text is required")
    private String lyric;
}
