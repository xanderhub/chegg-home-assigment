package com.chegg.assignment.loaders;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.Question;
import com.chegg.assignment.repositories.QuestionRepository;
import com.opencsv.CSVReader;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Service("csvLoader")
public final class CsvQuestionLoaderImpl extends QuestionLoader {

    private static Logger LOG = LoggerFactory.getLogger(CsvQuestionLoaderImpl.class);

    private CSVReader csvReader;

    @Autowired
    public CsvQuestionLoaderImpl(final QuestionRepository questionRepository) {
        super(questionRepository);
    }

    @Override
    public void loadQuestions(final AsyncHttpClient client, final String url) throws ExecutionException, InterruptedException, IOException {
        List<Question> questionsList = new ArrayList<>();
        client.prepareGet(url)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        LOG.info("Fetching CSV data...");
                        csvReader = new CSVReader(new StringReader(response.getResponseBody()));
                        csvReader.readNext(); //skip csv headers
                        String [] nextLine;
                        while ((nextLine = csvReader.readNext()) != null) {
                            Question question = new Question(nextLine[1]);
                            question.setSource(DataSource.CSV);
                            questionsList.add(question);
                        }
                        csvReader.close();
                        return response;
                    }
                })
        .get();
        questionRepository.saveAll(questionsList);
        LOG.info("CSV data loaded successfully!");
    }


}
