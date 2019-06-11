package com.chegg.assignment.domain.dto;

public final class QuestionDTO {
    private String value;
    private String source;

    public QuestionDTO(String value, String source) {
        this.value = value;
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
