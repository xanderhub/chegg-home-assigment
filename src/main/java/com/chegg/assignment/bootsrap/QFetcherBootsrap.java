package com.chegg.assignment.bootsrap;

import com.chegg.assignment.loaders.*;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class QFetcherBootsrap implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger LOG = LoggerFactory.getLogger(QFetcherBootsrap.class);

    private final AsyncHttpClient client ;

    @Value("${qfetcher.datasource.json}")
    private String jsonDataUrl;

    @Value("${qfetcher.datasource.csv}")
    private String csvDataUrl;

    @Value("${qfetcher.datasource.image}")
    private String imageDataUrl;

    private final QuestionLoader jsonQuestionLoader, csvQuestionLoader, imageQuestionLoader;

    @Autowired
    public QFetcherBootsrap(@Qualifier("jsonLoader") QuestionLoader jsonQuestionLoader
                            ,@Qualifier("csvLoader") QuestionLoader csvQuestionLoader
                            ,@Qualifier("imageLoader") QuestionLoader imageQuestionLoader) {
        this.client = Dsl.asyncHttpClient();
        this.jsonQuestionLoader = jsonQuestionLoader;
        this.csvQuestionLoader = csvQuestionLoader;
        this.imageQuestionLoader = imageQuestionLoader;

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            LOG.info("Start fetching data from multiple data sources...");
            jsonQuestionLoader.loadQuestions(client,jsonDataUrl);
            csvQuestionLoader.loadQuestions(client,csvDataUrl);
            imageQuestionLoader.loadQuestions(client, imageDataUrl);
        } catch (ExecutionException | InterruptedException | IOException e) {
            LOG.error("Failed to fetch / load data");
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
