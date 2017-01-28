package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;

@Entity
public class Choices {
    private long id;
    private long choiceQuestionId;
    private String text;
    private String isCorrect;
    private Questions questionsByChoiceQuestionId;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "choice_question_id", nullable = false)
    public long getChoiceQuestionId() {
        return choiceQuestionId;
    }

    public void setChoiceQuestionId(long choiceQuestionId) {
        this.choiceQuestionId = choiceQuestionId;
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
    @Column(name = "is_correct", nullable = true, length = 45)
    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choices choices = (Choices) o;

        if (id != choices.id) return false;
        if (choiceQuestionId != choices.choiceQuestionId) return false;
        if (text != null ? !text.equals(choices.text) : choices.text != null) return false;
        if (isCorrect != null ? !isCorrect.equals(choices.isCorrect) : choices.isCorrect != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (choiceQuestionId ^ (choiceQuestionId >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (isCorrect != null ? isCorrect.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "choice_question_id", referencedColumnName = "id", nullable = false)
    public Questions getQuestionsByChoiceQuestionId() {
        return questionsByChoiceQuestionId;
    }

    public void setQuestionsByChoiceQuestionId(Questions questionsByChoiceQuestionId) {
        this.questionsByChoiceQuestionId = questionsByChoiceQuestionId;
    }
}
