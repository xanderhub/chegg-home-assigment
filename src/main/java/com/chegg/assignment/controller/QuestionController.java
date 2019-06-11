package com.chegg.assignment.controller;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.dto.ResponseDTO;
import com.chegg.assignment.services.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api")
public class QuestionController {

    private static Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(value = "/questions", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO getQuestions(
            @RequestParam(value = "source", required = false) DataSource source,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            HttpServletRequest request) {
        LOG.info("GET Request: {}?{}", request.getRequestURI(), request.getQueryString());
        if(source == null)
            return questionService.getAllQuestions(request, page, size);
        else
            return questionService.getQuestionsBySource(source, request, page, size);
    }
}
