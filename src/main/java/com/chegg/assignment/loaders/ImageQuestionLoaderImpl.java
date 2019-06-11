package com.chegg.assignment.loaders;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.Question;
import com.chegg.assignment.repositories.QuestionRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

@Component
@Service("imageLoader")
public final class ImageQuestionLoaderImpl extends QuestionLoader {

    private static Logger LOG = LoggerFactory.getLogger(ImageQuestionLoaderImpl.class);

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${google.api.ocr}")
    private String googleOcrUrl;

    @Autowired
    public ImageQuestionLoaderImpl(QuestionRepository questionRepository) {
        super(questionRepository);
    }

    @Override
    public void loadQuestions(AsyncHttpClient client, String url) throws ExecutionException, InterruptedException, IOException {
        Request req = new RequestBuilder("POST")
                .setUrl(googleOcrUrl)
                .addHeader("Content-Type", "application/json")
                .addQueryParam("key", googleApiKey)
                .setBody("{\"requests\": [{\"features\": [{\"type\":\"TEXT_DETECTION\"}]," +
                        "\"image\": {\"source\": {\"imageUri\": \"" + url + "\"}}}]}")
                .build();

        client.prepareRequest(req).execute(
                new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response){
                        LOG.info("Fetching IMAGE data...");
                        JsonObject jsonObject = gson.fromJson(new StringReader(response.getResponseBody()), JsonObject.class);
                        JsonElement jsonElement = jsonObject.getAsJsonArray("responses").get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                        Question question = gson.fromJson(jsonElement, Question.class);
                        question.setSource(DataSource.IMAGE);
                        questionsList.add(question);
                        return response;
                    }
                }).get();

        questionRepository.saveAll(questionsList);
        LOG.info("IMAGE data loaded successfully!");
    }
}

