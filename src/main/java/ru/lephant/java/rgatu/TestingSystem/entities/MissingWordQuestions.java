package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "missing_word_questions", schema = "testing_system", catalog = "")
public class MissingWordQuestions {
    private long id;
    private String possibleAnswers;
    private Questions questionsById;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "possible_answers", nullable = false, length = 1024)
    public String getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(String possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MissingWordQuestions that = (MissingWordQuestions) o;

        if (id != that.id) return false;
        if (possibleAnswers != null ? !possibleAnswers.equals(that.possibleAnswers) : that.possibleAnswers != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (possibleAnswers != null ? possibleAnswers.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    public Questions getQuestionsById() {
        return questionsById;
    }

    public void setQuestionsById(Questions questionsById) {
        this.questionsById = questionsById;
    }
}
