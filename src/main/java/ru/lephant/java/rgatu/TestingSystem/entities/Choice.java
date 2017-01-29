package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "choices")
public class Choice implements Serializable {
    private long id;
    private Question question;
    private String text;
    private boolean correctIt;


    public Choice() {
    }

    public Choice(Question question) {
        this.question = question;
    }

    public Choice(Question question, String text, boolean correctIt) {
        this.question = question;
        this.text = text;
        this.correctIt = correctIt;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "choice_question_id", referencedColumnName = "id")
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "is_correct", nullable = false)
    public boolean isCorrectIt() {
        return correctIt;
    }

    public void setCorrectIt(boolean correctIt) {
        this.correctIt = correctIt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choice choice = (Choice) o;

        if (id != choice.id) return false;
        if (correctIt != choice.correctIt) return false;
        if (!question.equals(choice.question)) return false;
        return text.equals(choice.text);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + question.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (correctIt ? 1 : 0);
        return result;
    }
}