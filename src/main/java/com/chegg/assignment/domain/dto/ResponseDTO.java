package com.chegg.assignment.domain.dto;

import java.util.ArrayList;
import java.util.List;

public final class ResponseDTO {
    public final List<QuestionDTO> questions = new ArrayList<>();
    public final PaginationDTO pagination = new PaginationDTO();

    public final class PaginationDTO {
        public String next;
    }
}
