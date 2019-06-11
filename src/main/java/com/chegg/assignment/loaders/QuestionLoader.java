package com.chegg.assignment.loaders;

import com.chegg.assignment.domain.Question;
import com.chegg.assignment.repositories.QuestionRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public abstract class QuestionLoader {
    final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    final List<Question> questionsList = new ArrayList<>();

    final QuestionRepository questionRepository;

    @Autowired
    public QuestionLoader(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public abstract void loadQuestions(AsyncHttpClient client, String url) throws ExecutionException, InterruptedException, IOException;
}
