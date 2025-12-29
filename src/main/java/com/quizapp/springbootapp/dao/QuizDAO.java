package com.quizapp.springbootapp.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.springbootapp.entity.Quiz;

@Repository
public interface QuizDAO extends JpaRepository<Quiz, Integer> {

    Quiz findByTitle(String title);

}
