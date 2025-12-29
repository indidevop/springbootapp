package com.quizapp.springbootapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizapp.springbootapp.dao.QuestionDAO;
import com.quizapp.springbootapp.entity.Question;

@Service
public class QuestionService {

	@Autowired
	QuestionDAO dao;

	public ResponseEntity<List<Question>> getAllQuestions() {

		try {
			List<Question> lst = dao.findAll();
			return new ResponseEntity<>(lst, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<Question>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {

		try {
			List<Question> lst = dao.findByCategory(category);
			return new ResponseEntity<>(lst, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<Question>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) {

		try {
			Question q = dao.save(question);
		    if (q != null) {
			return new ResponseEntity<>("Success",HttpStatus.CREATED);
		    }
			else{
				return new ResponseEntity<>("Not added",HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
		
	}

}
