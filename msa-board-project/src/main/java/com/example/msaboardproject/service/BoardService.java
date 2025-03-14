package com.example.msaboardproject.service;

import com.example.msaboardproject.dto.BoardDeleteRequestDTO;
import com.example.msaboardproject.mapper.BoardMapper;
import com.example.msaboardproject.model.Article;
import com.example.msaboardproject.model.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final FileService fileService;

    @Transactional(readOnly=true)
    public List<Article> getArticles(int page, int size, int firstId, int lastId, String excBtn){
        if(excBtn.equals("prev")){
            return boardMapper.getBoardArticles(
                    Paging.builder()
                            .lastId(firstId)
                            .size(size)
                            .build()
            );
        }else{
            return boardMapper.getBoardArticles(
                    Paging.builder()
                            .lastId(lastId)
                            .size(size)
                            .build()
            );
        }
    }

    @Transactional(readOnly=true)
    public int totalArticleCnt(){
        return boardMapper.getArticleCnt();
    }

    @Transactional
    public void saveArticle(String userId, String title, String content, MultipartFile file) {
        String path = null;
        if(!file.isEmpty()){
            path = fileService.fileUpload(file);
        }

        boardMapper.saveArticle(
                Article.builder()
                        .userId(userId)
                        .title(title)
                        .content(content)
                        .filePath(path)
                        .build()
        );
    }

    @Transactional(readOnly=true)
    public Article getBoardDetail(long id) {
        return boardMapper.getArticleById(id);
    }

    public Resource downloadFile(String fileName) {
        return fileService.downloadFile(fileName);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN') or #findUpdateArticle.userId == authentication.name")
    @Transactional
    public void updateArticle(
            long id,
            String title,
            String content,
            MultipartFile file,
            Boolean fileChanged,
            String filePath
    ) {
        String path = null;
        if(!file.isEmpty()){
            path = fileService.fileUpload(file);
        }
        if(fileChanged){
            fileService.deleteFile(path);
        }else{
            path = filePath;
        }

        boardMapper.updateArticle(
                Article.builder()
                        .id(id)
                        .title(title)
                        .content(content)
                        .updated(LocalDateTime.now())
                        .filePath(path)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteBoardById(long id, BoardDeleteRequestDTO requestDTO) {
        fileService.deleteFile(requestDTO.getFilePath());
        boardMapper.deleteBoardById(id);
    }
}
