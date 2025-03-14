package com.example.msaboardproject.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Paging {
    private int lastId;
    private int size;
}
