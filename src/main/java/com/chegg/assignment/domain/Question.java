package com.chegg.assignment.domain;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "questions", indexes = {@Index(name = "i_source", columnList = "source")})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Expose
    @Type(type = "text")
    private String text;

    @Enumerated(value = EnumType.STRING)
    private DataSource source;

    public Question(final String text) {
        this.text = text;
    }

    public Question() {
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(final DataSource source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}