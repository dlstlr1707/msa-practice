package com.example.msaboardproject.mapper;

import com.example.msaboardproject.model.Article;
import com.example.msaboardproject.model.Paging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<Article> getBoardArticles(Paging paging);
    int getArticleCnt();
    void saveArticle(Article article);
    Article getArticleById(long id);
    void updateArticle(Article article);
    void deleteBoardById(long id);
}
