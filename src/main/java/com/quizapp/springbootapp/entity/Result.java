package com.quizapp.springbootapp.entity;

import java.util.Map;

import lombok.Data;

@Data
public class Result {
    private Integer quizId;
    private String quizTitle;
    private Map<QuestionWrapper, String> correctAnswerMap;
    private Map<QuestionWrapper, String> markedAnswerMap;
    private Integer score=0;
}
