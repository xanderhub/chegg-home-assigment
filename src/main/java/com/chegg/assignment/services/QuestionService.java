package com.chegg.assignment.services;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.Question;
import com.chegg.assignment.domain.dto.QuestionDTO;
import com.chegg.assignment.domain.dto.ResponseDTO;
import com.chegg.assignment.repositories.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public ResponseDTO getAllQuestions(HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> result = questionRepository.findAll(pageable);
        return toResponseDTO(result, request, page);
    }

    public ResponseDTO getQuestionsBySource(final DataSource source, HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> result = questionRepository.findAllBySource(source, pageable);
        return toResponseDTO(result, request, page);
    }

    private ResponseDTO toResponseDTO(Page<Question> result, HttpServletRequest request, int page) {
        ResponseDTO response = new ResponseDTO();
        result.getContent().forEach(
                q -> response.questions.add(new QuestionDTO(q.getText(), q.getSource().toString())));
        response.pagination.next = result.isLast() ? null :
                request.getRequestURI() +"?"+ request.getQueryString()
                        .replace("page=" + page, "page=" + (++page));

        return response;
    }
}
