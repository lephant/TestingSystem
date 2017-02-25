package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "missing_possible_answer", schema = "testing_system", catalog = "")
public class MissingPossibleAnswer {
    private long id;
    private MissingWordQuestion missingWordQuestion;
    private String text;


    public MissingPossibleAnswer() {
    }

    public MissingPossibleAnswer(MissingWordQuestion missingWordQuestion) {
        this.missingWordQuestion = missingWordQuestion;
    }

    public MissingPossibleAnswer(MissingWordQuestion missingWordQuestion, String text) {
        this.missingWordQuestion = missingWordQuestion;
        this.text = text;
    }


    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "missing_word_question_id", referencedColumnName = "id")
    public MissingWordQuestion getMissingWordQuestion() {
        return missingWordQuestion;
    }

    public void setMissingWordQuestion(MissingWordQuestion missingWordQuestion) {
        this.missingWordQuestion = missingWordQuestion;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 255)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MissingPossibleAnswer that = (MissingPossibleAnswer) o;

        if (id != that.id) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
