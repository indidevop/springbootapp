package com.quizapp.springbootapp.entity;

import java.util.Map;

import lombok.Data;

@Data
public class ClientResponse {
    private Integer quizId;
    private Map<Integer, String> responseMap;
}
