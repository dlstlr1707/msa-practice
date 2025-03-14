package com.example.msaboardproject.model;

import com.example.msaboardproject.dto.BoardDetailResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Article {
    private Long id;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String filePath;

    public BoardDetailResponseDTO toBoardDetailResponseDTO() {
        return BoardDetailResponseDTO.builder()
                .title(title)
                .content(content)
                .created(created)
                .filePath(filePath)
                .userId(userId)
                .build();
    }
}
