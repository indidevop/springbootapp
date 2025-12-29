package com.quizapp.springbootapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.springbootapp.entity.ClientResponse;
import com.quizapp.springbootapp.entity.QuizWrapper;
import com.quizapp.springbootapp.entity.Result;
import com.quizapp.springbootapp.service.QuizService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/quiz")
public class QuizController {
    
    @Autowired
    QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ,
            @RequestParam String title) {
        return quizService.createQuiz(category,numQ,title);
    }

    @GetMapping("/getQuiz/{title}")
    public ResponseEntity<QuizWrapper> getQuizByTitle(@PathVariable String title) {
        return quizService.getQuizByTitle(title);
    }

    @PostMapping("/result")
    public ResponseEntity<Result> getResult(@RequestBody ClientResponse clientResponse) {
                return quizService.getResult(clientResponse);
    }
    
    
}
