package com.example.msaboardproject.controller;

import com.example.msaboardproject.dto.BoardDeleteRequestDTO;
import com.example.msaboardproject.dto.BoardDetailResponseDTO;
import com.example.msaboardproject.dto.BoardListResponseDTO;
import com.example.msaboardproject.model.Article;
import com.example.msaboardproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {
    private final BoardService boardService;

    @GetMapping
    public BoardListResponseDTO getBoards(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "firstId", defaultValue = "-1") int firstId,
            @RequestParam(name = "lastId", defaultValue = "0") int lastId,
            @RequestParam(name = "excBtn", defaultValue = "next") String excBtn
    ){
        List<Article> articles = boardService.getArticles(page, size, firstId, lastId, excBtn);

        int totalArticleCnt = boardService.totalArticleCnt();

        boolean last = (page * size) >= totalArticleCnt;

        return BoardListResponseDTO.builder()
                .articles(articles)
                .last(last)
                .build();
    }

    @GetMapping("/{id}")
    public BoardDetailResponseDTO getBoardDetail(@PathVariable long id) {
        return boardService
                .getBoardDetail(id)
                .toBoardDetailResponseDTO();
    }

    @PostMapping
    public void saveArticle(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("hiddenUserId") String userId,
            @RequestParam("file") MultipartFile file
    ){
        boardService.saveArticle(userId, title, content, file);
    }

    @GetMapping("/file/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = boardService.downloadFile(fileName);

        String encoded = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename*=UTF-8''" + encoded)
                .body(resource);
    }

    @PutMapping
    public void updateArticle(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("hiddenUserId") String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("hiddenId") Long id,
            @RequestParam("hiddenFileFlag") Boolean fileChanged,
            @RequestParam("hiddenFilePath") String filePath
    ){
        boardService.updateArticle(id, title, content, file, fileChanged, filePath);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable long id, @RequestBody BoardDeleteRequestDTO boardDeleteRequestDTO){
        boardService.deleteBoardById(id,boardDeleteRequestDTO);
    }
}
