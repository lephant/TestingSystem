package ru.lephant.java.rgatu.TestingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "multi_choice_questions", schema = "testing_system", catalog = "")
public class MultiChoiceQuestions {
    private long id;
    private Questions questionsById;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiChoiceQuestions that = (MultiChoiceQuestions) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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
