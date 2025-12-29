package com.quizapp.springbootapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizapp.springbootapp.dao.QuestionDAO;
import com.quizapp.springbootapp.dao.QuizDAO;
import com.quizapp.springbootapp.entity.ClientResponse;
import com.quizapp.springbootapp.entity.Question;
import com.quizapp.springbootapp.entity.QuestionWrapper;
import com.quizapp.springbootapp.entity.Quiz;
import com.quizapp.springbootapp.entity.QuizWrapper;
import com.quizapp.springbootapp.entity.Result;

@Service
public class QuizService {

    @Autowired
    QuizDAO quizDao;

    @Autowired
    QuestionDAO questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        try {
            List<Question> questions = questionDao.getRandomQuestions(category, numQ);

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);

            Quiz res = quizDao.save(quiz);

            if (res != null) {
                return new ResponseEntity<>("Quiz created successfully", HttpStatus.CREATED);
            }

            return new ResponseEntity<>("Some issue occurred", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Some issue occurred", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<QuizWrapper> getQuizByTitle(String title) {

        Quiz quizFromBackend = quizDao.findByTitle(title);

        List<Question> questionList = quizFromBackend.getQuestions();
        List<QuestionWrapper> questionWrapperList = new ArrayList<>();

        for (Question q : questionList) {
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(q.getId());
            questionWrapper.setOption1(q.getOption1());
            questionWrapper.setOption2(q.getOption2());
            questionWrapper.setOption3(q.getOption3());
            questionWrapper.setOption4(q.getOption4());
            questionWrapper.setQuestionTitle(q.getQuestionTitle());

            questionWrapperList.add(questionWrapper);
        }

        QuizWrapper quizToClient = new QuizWrapper();

        quizToClient.setId(quizFromBackend.getId());
        quizToClient.setTitle(quizFromBackend.getTitle());
        quizToClient.setQuestions(questionWrapperList);

        return new ResponseEntity<>(quizToClient, HttpStatus.OK);
    }

    public ResponseEntity<Result> getResult(ClientResponse clientResponse) {

        try {
            Result result = new Result();

            Map<QuestionWrapper, String> choosenAnswerMap = new HashMap<>();
            Map<QuestionWrapper, String> correctAnswerMap = new HashMap<>();

            Map<Integer, String> response = clientResponse.getResponseMap();
            Integer quizWrapperId = clientResponse.getQuizId();

            Optional<Quiz> quizOpt = quizDao.findById(quizWrapperId);

            Quiz quiz = quizOpt.get(); // unwrap from optional
            String quizTitle = quiz.getTitle();

            result.setQuizId(quizWrapperId);
            result.setQuizTitle(quizTitle);

            response.forEach((questionWrapperId, choosen) -> {
                
                Optional<Question> questionOpt = questionDao.findById(questionWrapperId);
                Question q = questionOpt.get();
                QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());

                if (choosen.equals(q.getRightAnswer())) {
                    result.setScore(result.getScore() + 1);
                }

                choosenAnswerMap.put(questionWrapper, choosen);
                correctAnswerMap.put(questionWrapper, q.getRightAnswer());
            });

            result.setCorrectAnswerMap(correctAnswerMap);
            result.setMarkedAnswerMap(choosenAnswerMap);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Result(), HttpStatus.EXPECTATION_FAILED);
        }
    }

}
