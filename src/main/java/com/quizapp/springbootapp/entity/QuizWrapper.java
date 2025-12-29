package com.quizapp.springbootapp.entity;

import java.util.List;

import lombok.Data;

@Data
public class QuizWrapper {
    private Integer id;
    private String title;
    private List<QuestionWrapper> questions;
}
