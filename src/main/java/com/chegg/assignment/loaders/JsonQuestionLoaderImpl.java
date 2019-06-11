package com.chegg.assignment.loaders;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.Question;
import com.chegg.assignment.repositories.QuestionRepository;
import com.google.gson.stream.JsonReader;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.concurrent.ExecutionException;

@Component
@Service("jsonLoader")
public final class JsonQuestionLoaderImpl extends QuestionLoader {
    private static Logger LOG = LoggerFactory.getLogger(JsonQuestionLoaderImpl.class);

    @Autowired
    public JsonQuestionLoaderImpl(final QuestionRepository questionRepository) {
        super(questionRepository);
    }

    @Override
    public void loadQuestions(final AsyncHttpClient client, final String url) throws ExecutionException, InterruptedException {
        client.prepareGet(url)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        LOG.info("Fetching JSON data...");
                        JsonReader reader = new JsonReader(new StringReader(response.getResponseBody()));
                        reader.beginObject();
                        reader.nextName();
                        reader.beginArray();
                        while(reader.hasNext()) {
                            Question question = gson.fromJson(reader, Question.class);
                            question.setSource(DataSource.JSON);
                            questionsList.add(question);
                        }
                        reader.close();
                        return response;
                    }
                }).get();
        questionRepository.saveAll(questionsList);
        LOG.info("JSON data loaded successfully!");
    }
}
