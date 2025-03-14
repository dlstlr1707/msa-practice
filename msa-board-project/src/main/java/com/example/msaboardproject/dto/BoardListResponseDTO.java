package com.example.msaboardproject.dto;

import com.example.msaboardproject.model.Article;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardListResponseDTO {
    List<Article> articles; // 추후 모델을 바로 보내지 말고 필요한 정보만 담는 클래스 하나 만들어서 응담
    boolean last;
}
