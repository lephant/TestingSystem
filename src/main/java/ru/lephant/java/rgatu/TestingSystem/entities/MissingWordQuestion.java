package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missing_word_questions")
@PrimaryKeyJoinColumn(name = "id")
public class MissingWordQuestion extends Question implements Serializable {

    private List<MissingPossibleAnswer> possibleAnswers = new ArrayList<>();
    private String answer;


    public MissingWordQuestion() {
    }

    public MissingWordQuestion(Test test, String text, int value) {
        super(test, text, value);
    }

    public MissingWordQuestion(Test test, String text, int value, Image image) {
        super(test, text, value, image);
    }


    @OneToMany(mappedBy = "missingWordQuestion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<MissingPossibleAnswer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<MissingPossibleAnswer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    @Transient
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MissingWordQuestion question = (MissingWordQuestion) o;

        return possibleAnswers != null ? possibleAnswers.equals(question.possibleAnswers) : question.possibleAnswers == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (possibleAnswers != null ? possibleAnswers.hashCode() : 0);
        return result;
    }
}